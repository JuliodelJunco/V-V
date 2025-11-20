package filesort.service.impl;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

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
        File temporal = File.createTempFile("bytes_example",".txt");
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
        File temporal = File.createTempFile("kilobytes_example",".txt");
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
        File temporal = File.createTempFile("megabytes_example",".txt");
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
        File temporal = File.createTempFile("gigabytes_example",".txt");
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
        File temporal = File.createTempFile("terabytes_example",".txt");
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
        File temporal = File.createTempFile("corrupted_example",".txt");
        temporal.deleteOnExit();

        RandomAccessFile raf = new RandomAccessFile(temporal, "rw");
        FileChannel channel = raf.getChannel();
        FileLock lock = channel.lock();

        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileSize(temporal.getAbsolutePath());

        assertEquals("0 bytes", result);
        IOException ex = assertThrows(IOException.class,
                () -> fileService.getFileSize(temporal.getAbsolutePath()));
        assertEquals("An error occurred", ex.getMessage());

        lock.release();
        raf.close();
    }

    @Test
    void TC08(){
        FileServiceImpl fileService = new FileServiceImpl();
        Exception ex = assertThrows(IOException.class, () -> fileService.getFileType("a"));
        assertEquals("No file given. Please provide a file.", ex.getMessage());
    }

    @Test
    void TC09() throws IOException {
        File temporal = File.createTempFile("example",".txt");
        temporal.deleteOnExit();


        FileServiceImpl fileService = new FileServiceImpl();
        String result = fileService.getFileType(temporal.getAbsolutePath());

        assertEquals("txt", result);
    }

    @Test
    void TC10(){
        FileServiceImpl fileService = new FileServiceImpl();
        Exception ex = assertThrows(IOException.class, () -> fileService.deleteFile("a"));
        assertEquals("No file given. Please provide a file.", ex.getMessage());
    }

    @Test
    void TC11() throws IOException {
        File temporal = File.createTempFile("example",".txt");
        temporal.deleteOnExit();

        FileServiceImpl fileService = new FileServiceImpl();
        boolean result = fileService.deleteFile(temporal.getAbsolutePath());

        assertTrue(result);
    }

    @Test
    void TC12() throws IOException {
        File temporal = File.createTempFile("example",".txt");
        temporal.deleteOnExit();

        RandomAccessFile raf = new RandomAccessFile(temporal, "rw");
        FileChannel channel = raf.getChannel();
        FileLock lock = channel.lock();

        FileServiceImpl fileService = new FileServiceImpl();

        IOException ex = assertThrows(IOException.class,
                () -> fileService.deleteFile(temporal.getAbsolutePath()));

        assertEquals("An error occurred while deleting", ex.getMessage());

        lock.release();
        raf.close();
    }
}