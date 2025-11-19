package filesort;

import filesort.service.CommandParser;
import filesort.service.FileService;
import filesort.service.SortService;
import filesort.service.impl.CommandParserImpl;
import filesort.service.impl.FileServiceImpl;
import filesort.service.impl.SortServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class FileSortTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private CommandParser parser;
    private FileService fileService;
    private SortService sortService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        parser = new CommandParserImpl();
        fileService = new FileServiceImpl();
        sortService = new SortServiceImpl();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private void invokeExecuteLine(String line) throws Exception {
        Method method = FileSort.class.getDeclaredMethod("executeLine", String.class, CommandParser.class, FileService.class, SortService.class);
        method.setAccessible(true);
        method.invoke(null, line, parser, fileService, sortService);
    }

    @Test
    void TC35_handleSize_NoArgs_PrintsError() throws Exception {
        invokeExecuteLine("size");
        assertEquals("No file given. Please provide a file.", outContent.toString().trim());
    }

    @Test
    void TC36_TC50_handleSize_WithArgs_PrintsSize() throws Exception {
        File temp = File.createTempFile("example", ".txt");
        temp.deleteOnExit();
        try (RandomAccessFile raf = new RandomAccessFile(temp, "rw")) {
            raf.setLength(1);
        }

        invokeExecuteLine("size " + temp.getAbsolutePath());
        assertEquals("1 bytes", outContent.toString().trim());
    }

    @Test
    void TC37_handleType_NoArgs_PrintsError() throws Exception {
        invokeExecuteLine("type");
        assertEquals("No file given. Please provide a file.", outContent.toString().trim());
    }

    @Test
    void TC38_TC51_handleType_WithArgs_PrintsType() throws Exception {
        File temp = File.createTempFile("example", ".txt");
        temp.deleteOnExit();

        invokeExecuteLine("type " + temp.getAbsolutePath());
        assertEquals("txt", outContent.toString().trim());
    }

    @Test
    void TC39_handleDelete_NoArgs_PrintsError() throws Exception {
        invokeExecuteLine("delete");
        assertEquals("No file given. Please provide a file.", outContent.toString().trim());
    }

    @Test
    void TC40_TC52_handleDelete_WithArgs_DeletesFile() throws Exception {
        File temp = File.createTempFile("todelete", ".txt");
        assertTrue(temp.exists());

        invokeExecuteLine("delete " + temp.getAbsolutePath());

        assertFalse(temp.exists(), "The file should be deleted");
    }

    @Test
    void TC41_handleSort_TooFewArgs_PrintsError() throws Exception {
        File temp = File.createTempFile("a", ".txt");
        temp.deleteOnExit();

        invokeExecuteLine("alphabetical " + temp.getAbsolutePath());
        assertEquals("Too few arguments. Please provide at least 2 files.", outContent.toString().trim());
    }

    @Test
    void TC42_handleSort_TooManyArgs_PrintsError() throws Exception {
        StringBuilder sb = new StringBuilder("alphabetical");
        for (int i = 0; i < 11; i++) {
            File t = File.createTempFile("f" + i, ".txt");
            t.deleteOnExit();
            sb.append(" ").append(t.getAbsolutePath());
        }

        invokeExecuteLine(sb.toString());
        assertEquals("Too many arguments. Please provide a maximum of 10 files.", outContent.toString().trim());
    }

    @Test
    void TC43_TC53_handleSort_CorrectArgs_PrintsSorted() throws Exception {
        File f1 = File.createTempFile("a", ".txt");
        File f2 = File.createTempFile("b", ".txt");
        f1.deleteOnExit();
        f2.deleteOnExit();

        invokeExecuteLine("alphabetical " + f1.getAbsolutePath() + " " + f2.getAbsolutePath());

        String output = outContent.toString().trim();
        assertTrue(output.contains(f1.getName()));
        assertTrue(output.contains(f2.getName()));
    }

    @Test
    void TC44_main_NoArgs_PrintsUsage() throws IOException {
        FileSort.main(new String[]{});
        assertEquals("Usage: FileSort <input-file>", outContent.toString().trim());
    }

    @Test
    void TC45_main_NullArg_ThrowsException() {
        assertThrows(NullPointerException.class, () -> FileSort.main(null));
    }

    @Test
    void TC49_handleHelp_PrintsHelp() throws Exception {
        invokeExecuteLine("help");
        assertTrue(outContent.toString().contains("Available commands:"));
        assertTrue(outContent.toString().contains("size <FILE>"));
    }

    @Test
    void TC48_executeLine_NullLine_PrintsError() throws Exception {
        invokeExecuteLine(null);

        assertEquals("Empty command line", outContent.toString().trim());
    }
}