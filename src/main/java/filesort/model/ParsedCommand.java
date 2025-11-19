package filesort.model;

import java.util.List;

public record ParsedCommand(filesort.model.ParsedCommand.CommandType type, SortCriteria sortCriteria,
                            List<String> args) {

    public enum CommandType {
        HELP,
        SIZE,
        TYPE,
        DELETE,
        SORT
    }
}
