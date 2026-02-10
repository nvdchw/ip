package buddy;

import buddy.command.ByeCommand;
import buddy.command.Command;
import buddy.command.DeadlineCommand;
import buddy.command.DeleteCommand;
import buddy.command.EventCommand;
import buddy.command.FindCommand;
import buddy.command.ListCommand;
import buddy.command.MarkCommand;
import buddy.command.TodoCommand;
import buddy.command.UnmarkCommand;

/**
 * Enum representing different command types.
 */
public enum CommandType {
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    FIND,
    BYE,
    ERROR,
    UNKNOWN;

    /**
     * Maps a Command instance to its corresponding CommandType.
     *
     * @param command the Command instance
     * @return the corresponding CommandType
     */
    public static CommandType fromCommand(Command command) {
        if (command instanceof ListCommand) {
            return LIST;
        }
        if (command instanceof MarkCommand) {
            return MARK;
        }
        if (command instanceof UnmarkCommand) {
            return UNMARK;
        }
        if (command instanceof DeleteCommand) {
            return DELETE;
        }
        if (command instanceof TodoCommand) {
            return TODO;
        }
        if (command instanceof DeadlineCommand) {
            return DEADLINE;
        }
        if (command instanceof EventCommand) {
            return EVENT;
        }
        if (command instanceof FindCommand) {
            return FIND;
        }
        if (command instanceof ByeCommand) {
            return BYE;
        }
        return UNKNOWN;
    }
}
