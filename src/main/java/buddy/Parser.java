package buddy;

import buddy.command.*;

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
        default -> throw new BuddyException("I don't recognize that command.");
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
        try {
            String numberStr = input.substring(commandLength).trim();
            int taskNumber = Integer.parseInt(numberStr);
            return taskNumber - 1; // Convert to 0-based index
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new BuddyException("Please provide a valid task number.");
        }
    }
    
    /**
     * Parses the description from a todo command.
     *
     * @param input the user input string
     * @return the task description
     * @throws BuddyException if the description is empty
     */
    public static String parseTodoDescription(String input) throws BuddyException {
        String description = input.length() > 5 ? input.substring(5).trim() : "";
        
        if (description.isEmpty()) {
            throw new BuddyException("A todo needs a description.");
        }
        
        return description;
    }
    
    /**
     * Parses a deadline command into description and deadline time.
     *
     * @param input the user input string
     * @return an array with [description, deadline]
     * @throws BuddyException if the format is invalid
     */
    public static String[] parseDeadline(String input) throws BuddyException {
        String content = input.length() > 9 ? input.substring(9).trim() : "";
        int byIndex = content.indexOf(" /by ");
        
        if (content.isEmpty() || byIndex == -1) {
            throw new BuddyException("Deadline format: deadline <desc> /by <time>");
        }
        
        String description = content.substring(0, byIndex).trim();
        String by = content.substring(byIndex + 5).trim();
        
        if (description.isEmpty() || by.isEmpty()) {
            throw new BuddyException("Deadline needs description and time.");
        }
        
        return new String[]{description, by};
    }
    
    /**
     * Parses an event command into description, start time, and end time.
     *
     * @param input the user input string
     * @return an array with [description, from, to]
     * @throws BuddyException if the format is invalid
     */
    public static String[] parseEvent(String input) throws BuddyException {
        String content = input.length() > 6 ? input.substring(6).trim() : "";
        int fromIndex = content.indexOf(" /from ");
        int toIndex = content.indexOf(" /to ");
        
        if (content.isEmpty() || fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BuddyException("Event format: event <desc> /from <start> /to <end>");
        }
        
        String description = content.substring(0, fromIndex).trim();
        String from = content.substring(fromIndex + 7, toIndex).trim();
        String to = content.substring(toIndex + 5).trim();
        
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new BuddyException("Event needs description, start, and end time.");
        }
        
        return new String[]{description, from, to};
    }
    
    /**
     * Parses the date from a find command.
     *
     * @param input the user input string
     * @return the date string
     * @throws BuddyException if the date is missing
     */
    public static String parseFindDate(String input) throws BuddyException {
        String dateString = input.length() > 5 ? input.substring(5).trim() : "";
        
        if (dateString.isEmpty()) {
            throw new BuddyException("Find format: find <date> (yyyy-MM-dd)");
        }
        
        return dateString;
    }
}