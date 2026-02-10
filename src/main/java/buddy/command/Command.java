package buddy.command;

import java.util.ArrayList;

import buddy.BuddyException;
import buddy.Parser;
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
     * Parses and validates a 0-based task index from user input.
     *
     * @param userInput the full user input
     * @param commandLength the command keyword length
     * @param taskList the task list for bounds checking
     * @return the validated 0-based index
     * @throws BuddyException if parsing or validation fails
     */
    protected int parseValidatedIndex(String userInput, int commandLength, TaskList taskList) throws BuddyException {
        int taskIndex = Parser.parseTaskNumber(userInput, commandLength);
        requireValidIndex(taskIndex, taskList);
        return taskIndex;
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
