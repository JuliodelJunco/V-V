package filesort.service.impl;

import filesort.model.SortCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SortServiceImplTest {

    private File temporal;
    private File temporal2;

    @AfterEach
    void cleanup() {
        if (temporal != null && temporal.exists()) temporal.delete();
        if (temporal2 != null && temporal2.exists()) temporal2.delete();
    }

    @Test
    void TC13_empty_filepaths_returns_empty() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();


        List<String> filepaths = new ArrayList<>();
        filepaths = sortService.sortFiles(filepaths, SortCriteria.ALPHABETICAL);

        assertTrue(filepaths.isEmpty());
    }

    @Test
    void TC14_file_not_found_ThrowsException() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = File.createTempFile("example",".txt");
        temporal.deleteOnExit();

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal.getAbsolutePath());
        filepaths.add("a");
        List<String> finalFilepaths = filepaths;

        IOException ex = assertThrows(IOException.class,
                () -> sortService.sortFiles(finalFilepaths,SortCriteria.ALPHABETICAL));
        assertEquals("File not found: a", ex.getMessage());
    }

    @Test
    void TC15_alphabetical_sort() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = new File(System.getProperty("java.io.tmpdir"), "b_example.txt");
        temporal.createNewFile();

        temporal2 = new File(System.getProperty("java.io.tmpdir"), "a_example.txt");
        temporal2.createNewFile();

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal.getAbsolutePath());
        filepaths.add(temporal2.getAbsolutePath());
        filepaths = sortService.sortFiles(filepaths, SortCriteria.ALPHABETICAL);

        assertEquals("a_example.txt", new File(filepaths.getFirst()).getName());
        assertEquals("b_example.txt", new File(filepaths.getLast()).getName());
    }

    @Test
    void TC16_reverse_alphabetical_sort() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = new File(System.getProperty("java.io.tmpdir"), "a_example.txt");
        temporal.createNewFile();

        temporal2 = new File(System.getProperty("java.io.tmpdir"), "b_example.txt");
        temporal2.createNewFile();

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal.getAbsolutePath());
        filepaths.add(temporal2.getAbsolutePath());
        filepaths = sortService.sortFiles(filepaths, SortCriteria.REVERSE_ALPHABETICAL);

        assertEquals("b_example.txt", new File(filepaths.getFirst()).getName());
        assertEquals("a_example.txt", new File(filepaths.getLast()).getName());
    }

    @Test
    void TC17_time_creation_sort() throws IOException{
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = new File(System.getProperty("java.io.tmpdir"), "first_example.txt");
        temporal.createNewFile();
        Files.setAttribute(temporal.toPath(),"basic:creationTime", FileTime.fromMillis(1000));

        temporal2 = new File(System.getProperty("java.io.tmpdir"), "last_example.txt");
        temporal2.createNewFile();
        Files.setAttribute(temporal2.toPath(),"basic:creationTime", FileTime.fromMillis(2000));

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal2.getAbsolutePath());
        filepaths.add(temporal.getAbsolutePath());
        filepaths = sortService.sortFiles(filepaths, SortCriteria.CREATED);

        assertEquals("first_example.txt",new File(filepaths.getFirst()).getName());
        assertEquals("last_example.txt", new File(filepaths.getLast()).getName());
    }

    @Test
    void TC18_reverse_time_creation_sort() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = new File(System.getProperty("java.io.tmpdir"), "first_example.txt");
        temporal.createNewFile();
        Files.setAttribute(temporal.toPath(),"basic:creationTime", FileTime.fromMillis(1000));

        temporal2 = new File(System.getProperty("java.io.tmpdir"), "last_example.txt");
        temporal2.createNewFile();
        Files.setAttribute(temporal2.toPath(),"basic:creationTime", FileTime.fromMillis(2000));

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal.getAbsolutePath());
        filepaths.add(temporal2.getAbsolutePath());
        filepaths = sortService.sortFiles(filepaths, SortCriteria.REVERSE_CREATED);

        assertEquals("last_example.txt", new File(filepaths.getFirst()).getName());
        assertEquals("first_example.txt", new File(filepaths.getLast()).getName());
    }

    @Test
    void TC19_last_modified_sort() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = new File(System.getProperty("java.io.tmpdir"), "last_example.txt");
        temporal.createNewFile();
        temporal.setLastModified(1000);

        temporal2 = new File(System.getProperty("java.io.tmpdir"), "first_example.txt");
        temporal2.createNewFile();
        temporal.setLastModified(500);

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal.getAbsolutePath());
        filepaths.add(temporal2.getAbsolutePath());
        filepaths = sortService.sortFiles(filepaths, SortCriteria.MODIFIED);

        assertEquals("first_example.txt", new File(filepaths.getFirst()).getName());
        assertEquals("last_example.txt", new File(filepaths.getLast()).getName());
    }

    @Test
    void TC20_first_modified_sort() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = new File(System.getProperty("java.io.tmpdir"), "last_example.txt");
        temporal.createNewFile();
        temporal.setLastModified(1000);

        temporal2 = new File(System.getProperty("java.io.tmpdir"), "first_example.txt");
        temporal2.createNewFile();
        temporal.setLastModified(500);

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal.getAbsolutePath());
        filepaths.add(temporal2.getAbsolutePath());
        filepaths = sortService.sortFiles(filepaths, SortCriteria.REVERSE_MODIFIED);

        assertEquals("last_example.txt", new File(filepaths.getFirst()).getName());
        assertEquals("first_example.txt", new File(filepaths.getLast()).getName());
    }

    @Test
    void TC21_other_case_ThrowsException() throws IOException {
        SortServiceImpl sortService = new SortServiceImpl();

        temporal = new File(System.getProperty("java.io.tmpdir"), "last_example.txt");
        temporal.createNewFile();

        temporal2 = new File(System.getProperty("java.io.tmpdir"), "first_example.txt");
        temporal2.createNewFile();

        List<String> filepaths = new ArrayList<>();
        filepaths.add(temporal.getAbsolutePath());
        filepaths.add(temporal2.getAbsolutePath());

        List<String> finalFilepaths = filepaths;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sortService.sortFiles(finalFilepaths,SortCriteria.TESTING));
        assertEquals("Unsupported sort criteria: TESTING", ex.getMessage());
    }
}
