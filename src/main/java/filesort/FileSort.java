package filesort;

import filesort.model.ParsedCommand;
import filesort.model.SortCriteria;
import filesort.service.CommandParser;
import filesort.service.FileService;
import filesort.service.SortService;
import filesort.service.impl.CommandParserImpl;
import filesort.service.impl.FileServiceImpl;
import filesort.service.impl.SortServiceImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileSort {

    private static final String USAGE = "Usage: FileSort <input-file>";

    private static final String MSG_NO_FILE = "No file given. Please provide a file.";
    private static final String MSG_TOO_FEW =
            "Too few arguments. Please provide at least 2 files.";
    private static final String MSG_TOO_MANY =
            "Too many arguments. Please provide a maximum of 10 files.";

    private static final String HELP_TEXT = """
            Available commands:
              help
              size <FILE>
              type <FILE>
              delete <FILE>
              alphabetical <FILE1> <FILE2> ... <FILE10>
              reverse_alphabetical <FILE1> <FILE2> ... <FILE10>
              created <FILE1> <FILE2> ... <FILE10>
              reverse_created <FILE1> <FILE2> ... <FILE10>
              modified <FILE1> <FILE2> ... <FILE10>
              reverse_modified <FILE1> <FILE2> ... <FILE10>
            """;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println(USAGE);
            return;
        }

        FileService fileService = new FileServiceImpl();
        SortService sortService = new SortServiceImpl();
        CommandParser parser = new CommandParserImpl();

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeLine(line, parser, fileService, sortService);
            }
        }
    }

    private static void executeLine(String line,
                                    CommandParser parser,
                                    FileService fileService,
                                    SortService sortService) {
        try {
            ParsedCommand cmd = parser.parse(line);
            switch (cmd.type()) {
                case HELP -> printHelp();
                case SIZE -> handleSize(cmd.args(), fileService);
                case TYPE -> handleType(cmd.args(), fileService);
                case DELETE -> handleDelete(cmd.args(), fileService);
                case SORT -> handleSort(cmd.args(), cmd.sortCriteria(), sortService);
            }
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println(HELP_TEXT);
    }

    private static void handleSize(List<String> args, FileService fileService) throws IOException {
        if (args.isEmpty()) {
            System.out.println(MSG_NO_FILE);
            return;
        }
        String result = fileService.getFileSize(args.getFirst());
        System.out.println(result);
    }

    private static void handleType(List<String> args, FileService fileService) throws IOException {
        if (args.isEmpty()) {
            System.out.println(MSG_NO_FILE);
            return;
        }
        String result = fileService.getFileType(args.getFirst());
        System.out.println(result);
    }

    private static void handleDelete(List<String> args, FileService fileService) throws IOException {
        if (args.isEmpty()) {
            System.out.println(MSG_NO_FILE);
            return;
        }
        fileService.deleteFile(args.getFirst());
    }

    private static void handleSort(List<String> args,
                                   SortCriteria criteria,
                                   SortService sortService) throws IOException {
        int size = args.size();
        if (size < 2) {
            System.out.println(MSG_TOO_FEW);
            return;
        }
        if (size > 10) {
            System.out.println(MSG_TOO_MANY);
            return;
        }
        List<String> sorted = sortService.sortFiles(args, criteria);
        sorted.forEach(System.out::println);
    }
}