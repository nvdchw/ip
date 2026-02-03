package buddy.task;

import buddy.BuddyException;

/**
 * TaskParser handles parsing tasks from file format strings.
 */
public class TaskParser {
    /**
     * Parses a task from a file format string.
     *
     * @param line the line from the file in format: "type | isDone | description | ..."
     * @return the parsed task
     * @throws BuddyException if the line format is invalid
     */
    public static Task parseFromFile(String line) throws BuddyException {
        // Check for valid format
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new BuddyException("Invalid file format");
        }

        // Extract common fields
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        // Create task based on type
        Task task = switch (type) {
        case "T" -> new Todo(description);
        case "D" -> {
            if (parts.length < 4) {
                throw new BuddyException("Invalid deadline format");
            }
            yield new Deadline(description, parts[3]);
        }
        case "E" -> {
            if (parts.length < 5) {
                throw new BuddyException("Invalid event format");
            }
            yield new Event(description, parts[3], parts[4]);
        }
        default -> throw new BuddyException("Unknown task type: " + type);
        };

        // Set completion status
        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
