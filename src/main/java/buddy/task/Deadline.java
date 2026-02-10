package buddy.task;

import java.time.LocalDateTime;

/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a Deadline task with the given description and due date.
     *
     * @param description the description of the deadline task
     * @param by the due date of the deadline task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = DateTimeUtil.parseFileDateTime(by);
    }

    /**
     * Gets the due date and time of the deadline task.
     *
     * @return the due date and time as a LocalDateTime object
     */
    public LocalDateTime getDateTime() {
        return by;
    }

    @Override
    public String toFileFormat() {
        return TaskFormat.TYPE_DEADLINE + TaskFormat.DELIMITER + super.toFileFormat()
                + TaskFormat.DELIMITER + DateTimeUtil.formatFileDateTime(by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeUtil.formatDisplayDateTime(by) + ")";
    }
}
