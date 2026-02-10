package buddy.task;

import java.time.LocalDateTime;

/**
 * Represents an event task with a description, start time, and end time.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs an Event task with the given description, start time, end time, and tag.
     *
     * @param description the description of the event task
     * @param from the start time of the event
     * @param to the end time of the event
     * @param tag the optional tag for the event task
     */
    public Event(String description, String from, String to, String tag) {
        super(description, tag);
        this.from = DateTimeUtil.parseFileDateTime(from);
        this.to = DateTimeUtil.parseFileDateTime(to);
    }

    /**
     * Gets the start time of the event task.
     *
     * @return the start time as a LocalDateTime object
     */
    public LocalDateTime getStartTime() {
        return from;
    }

    /**
     * Gets the end time of the event task.
     *
     * @return the end time as a LocalDateTime object
     */
    public LocalDateTime getEndTime() {
        return to;
    }

    @Override
    public String toFileFormat() {
        return TaskFormat.TYPE_EVENT + TaskFormat.DELIMITER + super.toFileFormat()
            + TaskFormat.DELIMITER + DateTimeUtil.formatFileDateTime(from)
            + TaskFormat.DELIMITER + DateTimeUtil.formatFileDateTime(to)
            + formatTagForFile();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateTimeUtil.formatDisplayDateTime(from)
            + " to: " + DateTimeUtil.formatDisplayDateTime(to) + ")";
    }
}
