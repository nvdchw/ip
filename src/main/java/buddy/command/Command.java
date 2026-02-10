package buddy.command;

import java.util.ArrayList;

import buddy.BuddyException;
import buddy.Storage;
import buddy.Ui;
import buddy.task.TaskList;

/**
 * Abstract base class for all commands.
 */
public abstract class Command {
    /**
     * Executes the command.
     *
     * @param taskList the task list to operate on
     * @param ui the user interface for output
     * @param storage the storage for saving/loading tasks
     * @throws BuddyException if the command execution fails
     */
    public abstract void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException;
    /**
     * Checks if this command should exit the application.
     *
     * @return true if the application should exit, false otherwise
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Validates that the task index is within bounds.
     *
     * @param taskIndex the 0-based index to validate
     * @param taskList the task list to check against
     * @throws BuddyException if the index is invalid
     */
    protected void requireValidIndex(int taskIndex, TaskList taskList) throws BuddyException {
        if (taskIndex < 0 || taskIndex >= taskList.size()) {
            throw new BuddyException("Task number does not exist.");
        }
    }
    /**
     * Saves the task list to storage.
     * @param taskList The task list to save.
     * @param ui The user interface for error display.
     * @param storage The storage handler.
     */
    protected void saveTasks(TaskList taskList, Ui ui, Storage storage) {
        try {
            storage.save(new ArrayList<>(taskList.toFileFormat()));
        } catch (BuddyException e) {
            ui.showError("Error saving tasks: " + e.getMessage());
        }
    }
}
