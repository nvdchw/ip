package buddy.task;

/**
 * Represents a task with a description and completion status.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Constructs a Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the completion status icon for this task.
     *
     * @return "X" if the task is done, " " (space) if not done
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns a file format representation of the task.
     *
     * @return the file format string
     */
    public String toFileFormat() {
        String doneFlag = isDone ? TaskFormat.DONE_FLAG_TRUE : TaskFormat.DONE_FLAG_FALSE;
        return doneFlag + TaskFormat.DELIMITER + description;
    }

    /**
     * Returns a string representation of this task.
     *
     * @return a formatted string containing the status icon and task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
