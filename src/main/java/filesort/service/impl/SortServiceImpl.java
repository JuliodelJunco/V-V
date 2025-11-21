package filesort.service.impl;
import filesort.model.SortCriteria;
import filesort.service.SortService;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortServiceImpl implements SortService {

    @Override
    public List<String> sortFiles(List<String> filePaths, SortCriteria criteria) throws IOException {
        List<PathWithAttributes> paths = preparePaths(filePaths);
        Comparator<PathWithAttributes> comparator = buildComparator(criteria);
        return paths.stream()
                .sorted(comparator)
                .map(pathWithAttributes -> pathWithAttributes.path.toString())
                .collect(Collectors.toList());
    }

    private List<PathWithAttributes> preparePaths(List<String> filePaths) throws IOException {
        List<PathWithAttributes> paths = new ArrayList<>();
        for (String filePath : filePaths) {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new IOException("File not found: " + filePath);
            }
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            paths.add(new PathWithAttributes(path, attributes));
        }
        return paths;
    }

    private Comparator<PathWithAttributes> buildComparator(SortCriteria criteria) {
        switch (criteria) {
            case ALPHABETICAL:
                return Comparator.comparing(path -> path.path.getFileName().toString());
            case REVERSE_ALPHABETICAL:
                return Comparator.comparing((PathWithAttributes path) -> path.path.getFileName().toString()).reversed();
            case CREATED:
                return Comparator.comparing(this::creationTime);
            case REVERSE_CREATED:
                return Comparator.comparing(this::creationTime).reversed();
            case MODIFIED:
                return Comparator.comparing(this::lastModifiedTime).reversed();
            case REVERSE_MODIFIED:
                return Comparator.comparing(this::lastModifiedTime);
            default:
                throw new IllegalArgumentException("Unsupported sort criteria: " + criteria);
        }
    }

    private FileTime creationTime(PathWithAttributes pathWithAttributes) {
        return pathWithAttributes.attributes.creationTime();
    }

    private FileTime lastModifiedTime(PathWithAttributes pathWithAttributes) {
        return pathWithAttributes.attributes.lastModifiedTime();
    }

    private static final class PathWithAttributes {
        private final Path path;
        private final BasicFileAttributes attributes;
        PathWithAttributes(Path path, BasicFileAttributes attributes) {
            this.path = path;
            this.attributes = attributes;
        }
    }
}
