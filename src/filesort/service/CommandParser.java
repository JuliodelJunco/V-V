package filesort.service;

import filesort.model.ParsedCommand;

public interface CommandParser {
    ParsedCommand parse(String line);
}
