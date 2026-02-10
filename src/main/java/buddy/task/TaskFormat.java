package buddy.task;

/**
 * Defines the serialization format for tasks.
 */
public final class TaskFormat {
    public static final String TYPE_TODO = "T";
    public static final String TYPE_DEADLINE = "D";
    public static final String TYPE_EVENT = "E";
    public static final String DONE_FLAG_TRUE = "1";
    public static final String DONE_FLAG_FALSE = "0";
    public static final String DELIMITER = " | ";
    public static final String DELIMITER_REGEX = " \\| ";

    private TaskFormat() {
    }
}
