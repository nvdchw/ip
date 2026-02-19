package buddy.command;

import buddy.BuddyException;
import buddy.CommandKeyword;
import buddy.Storage;
import buddy.Ui;
import buddy.task.Task;
import buddy.task.TaskList;

/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private final String userInput;
    /**
     * Constructs a DeleteCommand with the given user input.
     * @param userInput The full user input string for the delete command.
     */
    public DeleteCommand(String userInput) {
        this.userInput = userInput;
    }

    /**
     * Executes the delete command to remove a task.
     * @param taskList The list of tasks to delete from.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        int taskIndex = parseValidatedIndex(userInput, CommandKeyword.DELETE.length(), taskList);

        // Remove the task and inform the user
        Task deletedTask = taskList.removeTask(taskIndex);
        saveTasks(taskList, ui, storage);
        ui.printBox(
            "Got it! I've removed this task:",
            "  " + deletedTask,
            "Now you have " + taskList.size() + " tasks in the list."
        );
    }
}
