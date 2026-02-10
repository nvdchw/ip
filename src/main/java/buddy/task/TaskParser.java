package buddy.task;

import buddy.BuddyException;
import buddy.Constants;

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
        String[] parts = line.split(TaskFormat.DELIMITER_REGEX);
        requirePartsLength(parts, Constants.MIN_TASK_PARTS, "Invalid file format");

        // Extract common fields
        String type = parts[0];
        boolean isDone = parseDoneFlag(parts[1]);
        String description = parts[2];
        String tag = getOptionalTag(parts, Constants.MIN_TASK_PARTS);

        // Create task based on type
        Task task = switch (type) {
        case TaskFormat.TYPE_TODO -> new Todo(description, tag);
        case TaskFormat.TYPE_DEADLINE -> {
            requirePartsLength(parts, Constants.MIN_DEADLINE_PARTS, "Invalid deadline format");
            yield new Deadline(description, parts[3], getOptionalTag(parts, Constants.MIN_DEADLINE_PARTS));
        }
        case TaskFormat.TYPE_EVENT -> {
            requirePartsLength(parts, Constants.MIN_EVENT_PARTS, "Invalid event format");
            yield new Event(description, parts[3], parts[4], getOptionalTag(parts, Constants.MIN_EVENT_PARTS));
        }
        default -> throw new BuddyException("Unknown task type: " + type);
        };

        // Set completion status
        if (isDone) {
            task.markAsDone();
        }

        return task;
    }

    private static void requirePartsLength(String[] parts, int minLength, String errorMessage)
            throws BuddyException {
        if (parts.length < minLength) {
            throw new BuddyException(errorMessage);
        }
    }

    private static boolean parseDoneFlag(String flag) throws BuddyException {
        if (TaskFormat.DONE_FLAG_TRUE.equals(flag)) {
            return true;
        }
        if (TaskFormat.DONE_FLAG_FALSE.equals(flag)) {
            return false;
        }
        throw new BuddyException("Invalid done flag: " + flag);
    }

    private static String getOptionalTag(String[] parts, int minParts) {
        if (parts.length <= minParts) {
            return null;
        }
        return parts[parts.length - 1].trim();
    }
}
