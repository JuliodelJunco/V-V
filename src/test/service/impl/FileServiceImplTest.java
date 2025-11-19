package test.service.impl;

import filesort.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;


class FileServiceImplTest {

    @Test
    void TC01(){
        FileServiceImpl fileService = new FileServiceImpl();
        Exception ex = assertThrows(IOException.class, () -> fileService.getFileSize("a"));
        assertEquals("No file given. Please provide a file.", ex.getMessage());
    }

    @Test
    void TC02() throws IOException {
        File temporal = File.createTempFile("bytes_example","txt");
        temporal.deleteOnExit();

        try(RandomAccessFile random = new RandomAccessFile(temporal,"rw")) {
            random.setLength(1);
        }

        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileSize(temporal.getAbsolutePath());

        assertEquals("1 bytes", result);
    }

    @Test
    void TC03() throws IOException {
        File temporal = File.createTempFile("kilobytes_example","txt");
        temporal.deleteOnExit();

        try(RandomAccessFile random = new RandomAccessFile(temporal,"rw")) {
            random.setLength(1024L);
        }

        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileSize(temporal.getAbsolutePath());

        assertEquals("1.0 kilobytes", result);
    }

    @Test
    void TC04() throws IOException {
        File temporal = File.createTempFile("megabytes_example","txt");
        temporal.deleteOnExit();

        try(RandomAccessFile random = new RandomAccessFile(temporal,"rw")) {
            random.setLength(1024L * 1024);
        }

        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileSize(temporal.getAbsolutePath());

        assertEquals("1.0 megabytes", result);
    }

    @Test
    void TC05() throws IOException {
        File temporal = File.createTempFile("gigabytes_example","txt");
        temporal.deleteOnExit();

        try(RandomAccessFile random = new RandomAccessFile(temporal,"rw")) {
            random.setLength(1024L * 1024 * 1024);
        }

        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileSize(temporal.getAbsolutePath());

        assertEquals("1.0 gigabytes", result);
    }

    @Test
    void TC06() throws IOException {
        File temporal = File.createTempFile("terabytes_example","txt");
        temporal.deleteOnExit();

        try(RandomAccessFile random = new RandomAccessFile(temporal,"rw")) {
            random.setLength(1024L * 1024 * 1024 * 1024);
        }

        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileSize(temporal.getAbsolutePath());

        assertEquals("1.0 terabytes", result);
    }


    @Test
    void TC07() throws IOException{
        File temporal = File.createTempFile("corrupted_example","txt");
        temporal.deleteOnExit();

        temporal.setReadable(false, false);

        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileSize(temporal.getAbsolutePath());

        assertEquals("0 bytes", result);
        IOException ex = assertThrows(IOException.class,
                () -> fileService.getFileSize(temporal.getAbsolutePath()));
        assertEquals("An error occurred", ex.getMessage());
    }
}

