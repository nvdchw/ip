package buddy.task;

/**
 * Represents a todo task.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task with the given description.
     *
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Constructs a Todo task with the given description and tag.
     *
     * @param description the description of the todo task
     * @param tag the optional tag for the todo task
     */
    public Todo(String description, String tag) {
        super(description, tag);
    }
    @Override
    public String toFileFormat() {
        return TaskFormat.TYPE_TODO + TaskFormat.DELIMITER + super.toFileFormat() + formatTagForFile();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
