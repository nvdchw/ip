package buddy.command;

import buddy.BuddyException;
import buddy.Constants;
import buddy.Storage;
import buddy.Ui;
import buddy.task.TaskList;

/**
 * Command to unmark a task (mark as not done).
 */
public class UnmarkCommand extends Command {
    private final String userInput;
    /**
     * Constructs an UnmarkCommand with the given user input.
     * @param userInput The full user input string for the unmark command.
     */
    public UnmarkCommand(String userInput) {
        this.userInput = userInput;
    }
    /**
     * Executes the unmark command to mark a task as not done.
     * @param taskList The list of tasks to unmark from.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        int taskIndex = parseValidatedIndex(userInput, Constants.UNMARK_LENGTH, taskList);

        // Mark the task as not done
        taskList.getTask(taskIndex).markAsUndone();
        saveTasks(taskList, ui, storage);
        ui.printBox(
            "OK, I've marked this task as not done yet:",
            "  " + taskList.getTask(taskIndex)
        );
    }
}
