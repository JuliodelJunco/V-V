package filesort.service.impl;

import filesort.model.ParsedCommand;
import filesort.model.SortCriteria;
import filesort.service.CommandParser;

import java.util.Arrays;
import java.util.List;

public class CommandParserImpl implements CommandParser {

    @Override
    public ParsedCommand parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Empty command line");
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Empty command line");
        }

        String[] tokens = trimmed.split("\\s+");
        String command = tokens[0];
        List<String> args = Arrays.asList(Arrays.copyOfRange(tokens, 1, tokens.length));

        return switch (command) {
            case "help" -> new ParsedCommand(ParsedCommand.CommandType.HELP, null, args);
            case "size" -> new ParsedCommand(ParsedCommand.CommandType.SIZE, null, args);
            case "type" -> new ParsedCommand(ParsedCommand.CommandType.TYPE, null, args);
            case "delete" -> new ParsedCommand(ParsedCommand.CommandType.DELETE, null, args);
            case "alphabetical" -> new ParsedCommand(
                    ParsedCommand.CommandType.SORT,
                    SortCriteria.ALPHABETICAL,
                    args
            );
            case "reverse_alphabetical" -> new ParsedCommand(
                    ParsedCommand.CommandType.SORT,
                    SortCriteria.REVERSE_ALPHABETICAL,
                    args
            );
            case "created" -> new ParsedCommand(
                    ParsedCommand.CommandType.SORT,
                    SortCriteria.CREATED,
                    args
            );
            case "reverse_created" -> new ParsedCommand(
                    ParsedCommand.CommandType.SORT,
                    SortCriteria.REVERSE_CREATED,
                    args
            );
            case "modified" -> new ParsedCommand(
                    ParsedCommand.CommandType.SORT,
                    SortCriteria.MODIFIED,
                    args
            );
            case "reverse_modified" -> new ParsedCommand(
                    ParsedCommand.CommandType.SORT,
                    SortCriteria.REVERSE_MODIFIED,
                    args
            );
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }
}
