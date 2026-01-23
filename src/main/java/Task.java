/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    // Constructor to initialise undone task with description
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    // Marks the task as done
    public void markAsDone() {
        this.isDone = true;
    }

    // Marks the task as not done
    public void markAsUndone() {
        this.isDone = false;
    }

    // Returns completionstatus icon
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    // Returns string representation of the task
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}