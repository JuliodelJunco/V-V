package filestort.service;

import filestort.model.SortCriteria;

import java.io.IOException;
import java.util.List;

public interface SortService {
    /**
     * Sorts a list of file paths based on a given criterion.
     * @param filePaths The list of files to sort.
     * @param criteria The sorting rule to apply (e.g., ALPHABETICAL, CREATED).
     * @return A new list containing the file paths in the sorted order.
     * @throws IOException if any of the files do not exist or their metadata cannot be read.
     */
    List<String> sortFiles(List<String> filePaths, SortCriteria criteria) throws IOException;
}
