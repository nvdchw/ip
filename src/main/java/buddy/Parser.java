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
 * Parser handles parsing and interpreting user commands.
 */
public class Parser {

    /**
     * Parses user input to determine the command type.
     *
     * @param input the user input string
     * @return the command type
     */
    public static Command parseCommand(String input) throws BuddyException {
        assert input != null : "Input should not be null";
        assert !input.trim().isEmpty() : "Input should not be empty";
        String trimmed = input.trim();
        int space = trimmed.indexOf(' ');
        String keyword = (space == -1) ? trimmed : trimmed.substring(0, space);
        return switch (keyword.toLowerCase()) {
        case "list" -> new ListCommand();
        case "mark" -> new MarkCommand(input);
        case "unmark" -> new UnmarkCommand(input);
        case "delete" -> new DeleteCommand(input);
        case "todo" -> new TodoCommand(input);
        case "deadline" -> new DeadlineCommand(input);
        case "event" -> new EventCommand(input);
        case "find" -> new FindCommand(input);
        case "bye" -> new ByeCommand();
        default -> throw new BuddyException("I don't recognise that command.");
        };
    }
    /**
     * Extracts the task number from mark/unmark/delete commands.
     *
     * @param input the user input string
     * @param commandLength the length of the command keyword
     * @return the task index (0-based)
     * @throws BuddyException if the task number is invalid
     */
    public static int parseTaskNumber(String input, int commandLength) throws BuddyException {
        assert input != null : "Input should not be null";
        assert commandLength >= 0 : "Command length should be non-negative";
        try {
            String numberStr = input.substring(commandLength).trim();
            int taskNumber = Integer.parseInt(numberStr);
            return taskNumber - 1; // Convert to 0-based index
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new BuddyException("Please provide a valid task number.");
        }
    }
    /**
     * Parses a todo command into description and optional tag.
     *
     * @param input the user input string
     * @return an array with [description, tag]
     * @throws BuddyException if the description is empty
     */
    public static String[] parseTodo(String input) throws BuddyException {
        assert input != null : "Input should not be null";
        if (input.length() <= CommandKeyword.TODO.length()) {
            throw new BuddyException("A todo needs a description.");
        }
        String content = input.substring(CommandKeyword.TODO.length()).trim();
        String[] contentParts = splitTag(content);
        String description = contentParts[0];
        String tag = contentParts[1];
        if (description.isEmpty()) {
            throw new BuddyException("A todo needs a description.");
        }
        return new String[]{description, tag};
    }
    /**
     * Parses a deadline command into description and deadline time.
     *
     * @param input the user input string
     * @return an array with [description, deadline]
     * @throws BuddyException if the format is invalid
     */
    public static String[] parseDeadline(String input) throws BuddyException {
        assert input != null : "Input should not be null";
        if (input.length() <= CommandKeyword.DEADLINE.length()) {
            throw new BuddyException("Deadline format: deadline <desc> /by <time>");
        }
        String content = input.substring(CommandKeyword.DEADLINE.length()).trim();
        String[] contentParts = splitTag(content);
        String contentWithoutTag = contentParts[0];
        String tag = contentParts[1];

        int byIndex = contentWithoutTag.indexOf(Constants.DEADLINE_BY_DELIMITER);
        if (contentWithoutTag.isEmpty() || byIndex == -1) {
            throw new BuddyException("Deadline format: deadline <desc> /by <time>");
        }
        String description = contentWithoutTag.substring(0, byIndex).trim();
        String by = contentWithoutTag.substring(byIndex + Constants.DEADLINE_BY_DELIMITER.length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new BuddyException("Deadline needs description and time.");
        }
        return new String[]{description, by, tag};
    }
    /**
     * Parses an event command into description, start time, and end time.
     *
     * @param input the user input string
     * @return an array with [description, from, to]
     * @throws BuddyException if the format is invalid
     */
    public static String[] parseEvent(String input) throws BuddyException {
        assert input != null : "Input should not be null";
        if (input.length() <= CommandKeyword.EVENT.length()) {
            throw new BuddyException("Event format: event <desc> /from <start> /to <end>");
        }
        String content = input.substring(CommandKeyword.EVENT.length()).trim();
        String[] contentParts = splitTag(content);
        String contentWithoutTag = contentParts[0];
        String tag = contentParts[1];
        int fromIndex = contentWithoutTag.indexOf(Constants.EVENT_FROM_DELIMITER);
        int toIndex = contentWithoutTag.indexOf(Constants.EVENT_TO_DELIMITER);
        if (contentWithoutTag.isEmpty() || fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BuddyException("Event format: event <desc> /from <start> /to <end>");
        }
        String description = contentWithoutTag.substring(0, fromIndex).trim();
        String from = contentWithoutTag.substring(
                fromIndex + Constants.EVENT_FROM_DELIMITER.length(), toIndex).trim();
        String to = contentWithoutTag.substring(toIndex + Constants.EVENT_TO_DELIMITER.length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new BuddyException("Event needs description, start, and end time.");
        }
        return new String[]{description, from, to, tag};
    }
    /**
     * Parses the date from a find command.
     *
     * @param input the user input string
     * @return the date string
     * @throws BuddyException if the date is missing
     */
    public static String parseFindDate(String input) throws BuddyException {
        assert input != null : "Input should not be null";
        if (input.length() <= CommandKeyword.FIND.length()) {
            throw new BuddyException("Find format: find <keyword|date|#tag>");
        }
        String dateString = input.substring(CommandKeyword.FIND.length()).trim();
        if (dateString.isEmpty()) {
            throw new BuddyException("Find format: find <keyword|date|#tag>");
        }
        return dateString;
    }

    private static String[] splitTag(String content) throws BuddyException {
        int tagIndex = content.indexOf(Constants.TAG_DELIMITER);
        if (tagIndex == -1) {
            return new String[]{content, null};
        }
        if (content.indexOf(Constants.TAG_DELIMITER, tagIndex + Constants.TAG_DELIMITER.length()) != -1) {
            throw new BuddyException("Please provide only one tag.");
        }
        String tagValue = content.substring(tagIndex + Constants.TAG_DELIMITER.length()).trim();
        if (tagValue.isEmpty()) {
            throw new BuddyException("Tag format: /tag <tag>");
        }
        String contentWithoutTag = content.substring(0, tagIndex).trim();
        if (contentWithoutTag.isEmpty()) {
            return new String[]{"", tagValue};
        }
        return new String[]{contentWithoutTag, tagValue};
    }
}
