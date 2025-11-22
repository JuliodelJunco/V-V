package filesort.service.impl;

import filesort.service.FileService;
import java.awt.Desktop;

import java.io.File;
import java.io.IOException;

public class FileServiceImpl implements FileService {
    /**
     * Calculates and formats the size of a given file.
     *
     * @param filePath The path to the file.
     * @return A formatted string representing the size (e.g., "15.3 MB").
     * @throws IOException if the file does not exist or cannot be read.
     */
    @Override
//    public String getFileSize(String filePath) throws IOException {
//        File sizeFile = new File(filePath);
//        if (sizeFile.exists()){
//            long size = sizeFile.length();
//            if (size < Math.pow(1024, 1)) return size + " bytes";
//            else if (size < Math.pow(1024, 2)) return size / Math.pow(1024, 1) + " kilobytes";
//            else if (size < Math.pow(1024, 3)) return size / Math.pow(1024, 2) + " megabytes";
//            else if (size < Math.pow(1024, 4)) return size / Math.pow(1024, 3) + " gigabytes";
//            else if (size >= Math.pow(1024, 4)) return size / Math.pow(1024, 4) + " terabytes";
//            else throw new IOException("An error occurred");
//        }else throw new IOException("No file given. Please provide a file.");
//    }
    public String getFileSize(String filePath) throws IOException {
        File sizeFile = new File(filePath);
        if (sizeFile.exists()){
            long size = sizeFile.length();
            if (size < Math.pow(1024, 1)) return size + " bytes";
            else if (size < Math.pow(1024, 2) && size >= Math.pow(1024, 1)) return size / Math.pow(1024, 1) + " kilobytes";
            else if (size < Math.pow(1024, 3) && size >= Math.pow(1024, 2)) return size / Math.pow(1024, 2) + " megabytes";
            else if (size < Math.pow(1024, 4) && size >= Math.pow(1024, 3)) return size / Math.pow(1024, 3) + " gigabytes";
            else if (size >= Math.pow(1024, 4)) return size / Math.pow(1024, 4) + " terabytes";
            else throw new IOException("An error occurred");
        }else throw new IOException("No file given. Please provide a file.");
    }
//    public String getFileSize(String filePath) throws IOException {
//        File sizeFile = new File(filePath);
//
//        if (!sizeFile.exists()) {
//            throw new IOException("No file given. Please provide a file.");
//        }
//
//        long size = sizeFile.length();
//        double kb = Math.pow(1024, 1);
//        double mb = Math.pow(1024, 2);
//        double gb = Math.pow(1024, 3);
//        double tb = Math.pow(1024, 4);
//
//        if (size < kb) {
//            return size + " bytes";
//        } else if (size < mb) {
//            return size / kb + " kilobytes";
//        } else if (size < gb) {
//            return size / mb + " megabytes";
//        } else if (size < tb) {
//            return size / gb + " gigabytes";
//        } else if(size <= tb){
//            return size / tb + " terabytes";
//        }else{
//            throw new IOException("An error occurred");
//        }
//    }



    /**
     * Gets the type (extension) of a given file.
     * @param filePath The path to the file.
     * @return The file extension (e.g., "txt").
     * @throws IOException if the file does not exist.
     */
    @Override
    public String getFileType(String filePath) throws IOException{
        File typeFile = new File(filePath);
        if (typeFile.exists()){
            String name = typeFile.getName();
            int dot = name.lastIndexOf(".");
            return name.substring(dot+1);
        }else throw new IOException("No file given. Please provide a file.");
    }

    /**
     * Sends file to bin
     * @param filePath The path to the file to be deleted.
     * @return Boolean if correctly deleted
     * @throws IOException if the file does not exist or the operation fails.
     */
    @Override
    public Boolean deleteFile(String filePath) throws IOException{
        File deletableFile = new File(filePath);
        if (deletableFile.exists()){
            boolean deleted = Desktop.getDesktop().moveToTrash(deletableFile);
            if(!deleted) throw new IOException("An error occurred while deleting");
            else return deleted;
        }else throw new IOException("No file given. Please provide a file.");
    }
}
