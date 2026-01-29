package buddy.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {
    protected LocalDateTime by;
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    /**
     * Constructs a Deadline task with the given description and due date.
     *
     * @param description the description of the deadline task
     * @param by the due date of the deadline task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = parseDateTime(by);
    }

    private static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, FILE_FORMAT);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date/time format. Use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
        }
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
        return "D | " + super.toFileFormat() + " | " + by.format(FILE_FORMAT);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}