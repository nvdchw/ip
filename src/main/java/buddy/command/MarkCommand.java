package buddy.command;

import buddy.BuddyException;
import buddy.CommandKeyword;
import buddy.Storage;
import buddy.Ui;
import buddy.task.TaskList;

/**
 * Command to mark a task as done.
 */
public class MarkCommand extends Command {
    private final String userInput;
    /**
     * Constructs a MarkCommand with the given user input.
     * @param userInput The full user input string for the mark command.
     */
    public MarkCommand(String userInput) {
        this.userInput = userInput;
    }
    /**
     * Executes the mark command to mark a task as done.
     * @param taskList The list of tasks to mark from.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        updateTaskAndReport(
            userInput,
            CommandKeyword.MARK.length(),
            taskList,
            ui,
            storage,
            buddy.task.Task::markAsDone,
            "Awesome! You crushed this task: "
        );
    }
}
