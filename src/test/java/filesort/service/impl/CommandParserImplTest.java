package filesort.service.impl;

import filesort.model.ParsedCommand;
import filesort.model.SortCriteria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserImplTest {

    private final CommandParserImpl parser = new CommandParserImpl();

    @Test
    void TC22_parse_NullLine_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
        assertEquals("Empty command line", exception.getMessage());
    }

    @Test
    void TC23_parse_BlankLine_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parse("   "));
        assertEquals("Empty command line", exception.getMessage());
    }

    @Test
    void TC24_parse_Help_ReturnsHelpType() {
        ParsedCommand cmd = parser.parse("help");
        assertEquals(ParsedCommand.CommandType.HELP, cmd.type());
    }

    @Test
    void TC25_parse_Size_ReturnsSizeType() {
        ParsedCommand cmd = parser.parse("size");
        assertEquals(ParsedCommand.CommandType.SIZE, cmd.type());
    }

    @Test
    void TC26_parse_Type_ReturnsTypeType() {
        ParsedCommand cmd = parser.parse("type");
        assertEquals(ParsedCommand.CommandType.TYPE, cmd.type());
    }

    @Test
    void TC27_parse_Delete_ReturnsDeleteType() {
        ParsedCommand cmd = parser.parse("delete");
        assertEquals(ParsedCommand.CommandType.DELETE, cmd.type());
    }

    @Test
    void TC28_parse_Alphabetical_ReturnsSortTypeAndCriteria() {
        ParsedCommand cmd = parser.parse("alphabetical");
        assertEquals(ParsedCommand.CommandType.SORT, cmd.type());
        assertEquals(SortCriteria.ALPHABETICAL, cmd.sortCriteria());
    }

    @Test
    void TC29_parse_ReverseAlphabetical_ReturnsSortTypeAndCriteria() {
        ParsedCommand cmd = parser.parse("reverse_alphabetical");
        assertEquals(ParsedCommand.CommandType.SORT, cmd.type());
        assertEquals(SortCriteria.REVERSE_ALPHABETICAL, cmd.sortCriteria());
    }

    @Test
    void TC30_parse_Created_ReturnsSortTypeAndCriteria() {
        ParsedCommand cmd = parser.parse("created");
        assertEquals(ParsedCommand.CommandType.SORT, cmd.type());
        assertEquals(SortCriteria.CREATED, cmd.sortCriteria());
    }

    @Test
    void TC31_parse_ReverseCreated_ReturnsSortTypeAndCriteria() {
        ParsedCommand cmd = parser.parse("reverse_created");
        assertEquals(ParsedCommand.CommandType.SORT, cmd.type());
        assertEquals(SortCriteria.REVERSE_CREATED, cmd.sortCriteria());
    }

    @Test
    void TC32_parse_Modified_ReturnsSortTypeAndCriteria() {
        ParsedCommand cmd = parser.parse("modified");
        assertEquals(ParsedCommand.CommandType.SORT, cmd.type());
        assertEquals(SortCriteria.MODIFIED, cmd.sortCriteria());
    }

    @Test
    void TC33_parse_ReverseModified_ReturnsSortTypeAndCriteria() {
        ParsedCommand cmd = parser.parse("reverse_modified");
        assertEquals(ParsedCommand.CommandType.SORT, cmd.type());
        assertEquals(SortCriteria.REVERSE_MODIFIED, cmd.sortCriteria());
    }

    @Test
    void TC34_parse_UnknownCommand_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parse("a"));
        assertEquals("Unknown command: a", exception.getMessage());
    }
}