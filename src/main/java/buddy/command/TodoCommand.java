package buddy.command;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.task.Todo;

/**
 * Command to add a todo task.
 */
public class TodoCommand extends Command {
    private final String userInput;
    /**
     * Constructs a TodoCommand with the given user input.
     * @param userInput The full user input string for the todo command.
     */
    public TodoCommand(String userInput) {
        this.userInput = userInput;
    }
    /**
     * Executes the todo command to add a todo task.
     * @param taskList The list of tasks to add the todo to.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        String[] parts = Parser.parseTodo(userInput);
        String description = parts[0];
        String tag = parts[1];

        // Create and add the todo task
        Task task = new Todo(description, tag);
        addAndReport(task, taskList, ui, storage);
    }
}
