package filestort.service;

import java.io.File;
import java.io.IOException;

class FileService {
    /**
     * Calculates and formats the size of a given file.
     *
     * @param filePath The path to the file.
     * @return A formatted string representing the size (e.g., "15.3 MB").
     * @throws IOException if the file does not exist or cannot be read.
     */
    public String getFileSize(String filePath) throws IOException {
        File sizeFile = new File(filePath);
        if (sizeFile.exists()){
            long size = sizeFile.length();
            if (size < Math.pow(10, 3)) return size + " bytes";
            else if (size < Math.pow(10, 6) && size > Math.pow(10, 3)) return size / Math.pow(1024, 1) + " kilobytes";
            else if (size < Math.pow(10, 9) && size > Math.pow(10, 6)) return size / Math.pow(1024, 2) + " megabytes";
            else if (size < Math.pow(10, 12) && size > Math.pow(10, 9)) return size / Math.pow(1024, 3) + " gigabytes";
            else if (size > Math.pow(10, 12)) return size / Math.pow(1024, 4) + " terabytes";
            else throw new IOException("An error occurred");
        }else throw new IOException("No file given. Please provide a file.");
    }


    /**
     * Gets the type (extension) of a given file.
     * @param filePath The path to the file.
     * @return The file extension (e.g., "txt").
     * @throws IOException if the file does not exist.
     */
    public String getFileType(String filePath) throws IOException{
        File typeFile = new File(filePath);
        if (typeFile.exists()){
            String name = typeFile.getName();
            int dot = name.lastIndexOf(".");
            return name.substring(dot+1);
        }else throw new IOException("No file given. Please provide a file.");
    }

    /**
     * Moves a given file to the "bin/deleted" directory.
     * @param filePath The path to the file to be deleted.
     * @throws IOException if the file does not exist or the operation fails.
     */
    void deleteFile(String filePath) throws IOException{
        File deletableFile = new File(filePath);
        if (deletableFile.exists()){
            boolean deleted = deletableFile.delete();
            if(!deleted) throw new IOException("An error occurred while deleting");
        }else throw new IOException("No file given. Please provide a file.");
    }
}
