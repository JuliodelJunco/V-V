import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FileSort {
    private static final String USAGE = "Usage: FileSort <input-file>";
    private static final String NO_FILE_ERROR = "No file given. Please provide a file.";
    private static final String TOO_FEW_ARGS_ERROR = "Too few arguments. Please provide at least 2 files.";
    private static final String TOO_MANY_ARGS_ERROR = "Too many arguments. Please provide a maximum of 10 files.";
    private static final DecimalFormat SIZE_FORMAT = new DecimalFormat("#0.00");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".txt", ".json", ".csv");
    private static final int MIN_SORT_ARGS = 2;
    private static final int MAX_SORT_ARGS = 10;
    private static final String HELP_TEXT = """
            Available commands:
            help
                Prints this help message.
            size <file>
                Shows the file size in kB/MB/GB/TB depending on magnitude.
            type <file>
                Shows the detected file type (.txt, .json, .csv).
            delete <file>
                Moves the file into bin/deleted inside the project directory.
            alphabetical <file1> <file2> ... <file10>
                Sorts 2-10 files alphabetically by name.
            reverse_alphabetical <file1> <file2> ... <file10>
                Sorts 2-10 files in reverse alphabetical order.
            created <file1> <file2> ... <file10>
                Sorts 2-10 files by creation date (oldest first).
            reverse_created <file1> <file2> ... <file10>
                Sorts 2-10 files by creation date (newest first).
            modified <file1> <file2> ... <file10>
                Sorts 2-10 files by last modification date (newest first).
            reverse_modified <file1> <file2> ... <file10>
                Sorts 2-10 files by last modification date (oldest first).
            """;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            return;
        }

        Path inputPath = Paths.get(args[0]);
        if (!Files.exists(inputPath) || Files.isDirectory(inputPath)) {
            System.out.println("Input file not found: " + inputPath);
            return;
        }

        try {
            List<String> lines = Files.readAllLines(inputPath);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty()) {
                    continue;
                }
                processCommand(line);
            }
        } catch (IOException e) {
            System.out.println("Failed to read input file: " + e.getMessage());
        }
    }

    private static void processCommand(String line) {
        String[] tokens = line.split("\\s+");
        if (tokens.length == 0) {
            return;
        }

        String command = tokens[0];
        String[] commandArgs = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (command) {
            case "help" -> handleHelp();
            case "size" -> handleSingleFileCommand(commandArgs, FileSort::printFileSize);
            case "type" -> handleSingleFileCommand(commandArgs, FileSort::printFileType);
            case "delete" -> handleSingleFileCommand(commandArgs, FileSort::deleteFile);
            case "alphabetical" -> handleSortCommand(commandArgs, Comparator.comparing(FileInfo::name), "Alphabetical order");
            case "reverse_alphabetical" -> handleSortCommand(commandArgs, Comparator.comparing(FileInfo::name).reversed(), "Reverse alphabetical order");
            case "created" -> handleSortCommand(commandArgs, Comparator.comparing(FileInfo::creationMillis), "Created (oldest to newest)");
            case "reverse_created" -> handleSortCommand(commandArgs, Comparator.comparing(FileInfo::creationMillis).reversed(), "Created (newest to oldest)");
            case "modified" -> handleSortCommand(commandArgs, Comparator.comparing(FileInfo::lastModifiedMillis).reversed(), "Modified (newest to oldest)");
            case "reverse_modified" -> handleSortCommand(commandArgs, Comparator.comparing(FileInfo::lastModifiedMillis), "Modified (oldest to newest)");
            default -> System.out.println("Unknown command: " + command);
        }
    }

    private static void handleHelp() {
        System.out.println(HELP_TEXT);
    }

    private static void handleSingleFileCommand(String[] args, FileAction action) {
        if (args.length == 0) {
            System.out.println(NO_FILE_ERROR);
            return;
        }

        Optional<Path> file = validateFileArgument(args[0]);
        file.ifPresent(path -> {
            try {
                action.accept(path);
            } catch (IOException e) {
                System.out.println("Operation failed for " + path.getFileName() + ": " + e.getMessage());
            }
        });
    }

    private static void handleSortCommand(String[] args, Comparator<FileInfo> comparator, String description) {
        if (args.length < MIN_SORT_ARGS) {
            System.out.println(TOO_FEW_ARGS_ERROR);
            return;
        }
        if (args.length > MAX_SORT_ARGS) {
            System.out.println(TOO_MANY_ARGS_ERROR);
            return;
        }

        List<Path> files = new ArrayList<>();
        for (String arg : args) {
            Optional<Path> file = validateFileArgument(arg);
            if (file.isEmpty()) {
                return;
            }
            files.add(file.get());
        }

        List<FileInfo> infoList = new ArrayList<>();
        for (Path path : files) {
            try {
                BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                infoList.add(new FileInfo(path, attributes));
            } catch (IOException e) {
                System.out.println("Unable to read file metadata for " + path.getFileName() + ": " + e.getMessage());
                return;
            }
        }

        infoList.sort(comparator);
        String result = infoList.stream()
                .map(FileInfo::name)
                .collect(Collectors.joining(", "));
        System.out.println(description + ": " + result);
    }

    private static Optional<Path> validateFileArgument(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println(NO_FILE_ERROR);
            return Optional.empty();
        }

        String lowerName = fileName.toLowerCase(Locale.ROOT);
        boolean supported = ALLOWED_EXTENSIONS.stream().anyMatch(lowerName::endsWith);
        if (!supported) {
            System.out.println("Invalid file type. Allowed extensions are .txt, .json, .csv.");
            return Optional.empty();
        }

        Path path = Paths.get(fileName);
        if (!Files.exists(path) || Files.isDirectory(path)) {
            System.out.println("File not found: " + path);
            return Optional.empty();
        }

        return Optional.of(path);
    }

    private static void printFileSize(Path path) throws IOException {
        long bytes = Files.size(path);
        double sizeInKB = bytes / 1000.0;
        String unit = "kB";
        double value = sizeInKB;

        if (value >= 1000) {
            value /= 1000;
            unit = "MB";
        }
        if (value >= 1000 && unit.equals("MB")) {
            value /= 1000;
            unit = "GB";
        }
        if (value >= 1000 && unit.equals("GB")) {
            value /= 1000;
            unit = "TB";
        }

        System.out.println(path.getFileName() + " size: " + SIZE_FORMAT.format(value) + " " + unit);
    }

    private static void printFileType(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        String type = name.endsWith(".txt") ? "Text (.txt)"
                : name.endsWith(".json") ? "JSON (.json)"
                : "CSV (.csv)";
        System.out.println(path.getFileName() + " type: " + type);
    }

    private static void deleteFile(Path path) throws IOException {
        Path binDir = Paths.get("bin", "deleted");
        Files.createDirectories(binDir);

        Path target = binDir.resolve(path.getFileName());
        if (Files.exists(target)) {
            String fileName = path.getFileName().toString();
            String baseName = fileName;
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex >= 0) {
                baseName = fileName.substring(0, dotIndex);
                extension = fileName.substring(dotIndex);
            }

            int counter = 1;
            while (Files.exists(target)) {
                target = binDir.resolve(baseName + "_" + counter + extension);
                counter++;
            }
        }

        try {
            Files.move(path, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved " + path.getFileName() + " to " + target);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Unable to move file; a file with the same name already exists in bin/deleted.");
        }
    }

    private record FileInfo(Path path, BasicFileAttributes attributes) {
        String name() {
            return path.getFileName().toString();
        }

        long creationMillis() {
            return attributes.creationTime().toMillis();
        }

        long lastModifiedMillis() {
            return attributes.lastModifiedTime().toMillis();
        }
    }

    @FunctionalInterface
    private interface FileAction {
        void accept(Path path) throws IOException;
    }
}
