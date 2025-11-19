package filesort.service;

import java.io.IOException;

public interface FileService {
    /**
     * Calculates and formats the size of a given file.
     * @param filePath The path to the file.
     * @return A formatted string representing the size (e.g., "15.3 MB").
     * @throws IOException if the file does not exist or cannot be read.
     */
    String getFileSize(String filePath) throws IOException;

    /**
     * Gets the type (extension) of a given file.
     * @param filePath The path to the file.
     * @return The file extension (e.g., "txt").
     * @throws IOException if the file does not exist.
     */
    String getFileType(String filePath) throws IOException;

    /**
     * Sends file to bin
     * @param filePath The path to the file to be deleted.
     * @return Boolean if correctly deleted
     * @throws IOException if the file does not exist or the operation fails.
     */
    Boolean deleteFile(String filePath) throws IOException;
}
