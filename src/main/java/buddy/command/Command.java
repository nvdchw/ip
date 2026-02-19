package buddy.command;

import java.util.ArrayList;
import java.util.function.Consumer;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.Task;
import buddy.task.TaskList;

/**
 * Abstract base class for all commands.
 */
public abstract class Command {
    @FunctionalInterface
    protected interface TaskSupplier {
        Task get();
    }
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
        assert taskList != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        assert ui != null : "Ui should not be null";
        try {
            storage.save(new ArrayList<>(taskList.toFileFormat()));
        } catch (BuddyException e) {
            ui.showError("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Updates a task (mark/unmark), saves, and prints a confirmation message.
     *
     * @param userInput the full user input
     * @param commandLength the command keyword length
     * @param taskList the task list for lookup
     * @param ui the user interface for output
     * @param storage the storage handler for saving tasks
     * @param taskUpdater the update action to apply to the task
     * @param headerMessage the header message for the confirmation box
     * @throws BuddyException if parsing or validation fails
     */
    protected void updateTaskAndReport(
            String userInput,
            int commandLength,
            TaskList taskList,
            Ui ui,
            Storage storage,
            Consumer<Task> taskUpdater,
            String headerMessage) throws BuddyException {
        int taskIndex = parseValidatedIndex(userInput, commandLength, taskList);
        Task task = taskList.getTask(taskIndex);
        taskUpdater.accept(task);
        saveTasks(taskList, ui, storage);
        ui.printBox(
            headerMessage,
            "  " + task
        );
    }

    /**
     * Adds a task built by the supplier, handling parse errors, then reports.
     *
     * @param taskSupplier supplier that builds the task
     * @param taskList the task list to add to
     * @param ui the user interface for output
     * @param storage the storage handler for saving tasks
     * @throws BuddyException if task construction fails
     */
    protected void addAndReport(
            TaskSupplier taskSupplier,
            TaskList taskList,
            Ui ui,
            Storage storage) throws BuddyException {
        try {
            Task task = taskSupplier.get();
            taskList.addTask(task);
            saveTasks(taskList, ui, storage);
            ui.printBox(
                "Got it! I've added this task to your list:",
                "  " + task,
                "Now you have " + taskList.size() + " tasks in the list."
            );
        } catch (IllegalArgumentException e) {
            throw new BuddyException(e.getMessage());
        }
    }
}
