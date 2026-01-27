/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Constructs a Deadline task with the given description and due date.
     *
     * @param description the description of the deadline task
     * @param by the due date of the deadline task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}