/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {
    protected String by;

    // Constructor to initialise deadline task with description and due date
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}