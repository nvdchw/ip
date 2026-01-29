package buddy.command;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.*;

/**
 * Command to add a todo task.
 */
public class TodoCommand extends Command {
    private final String userInput;
    
    /**
     * Constructs a TodoCommand with the given user input.
     * 
     * @param userInput The full user input string for the todo command.
     */
    public TodoCommand(String userInput) {
        this.userInput = userInput;
    }
    
    /**
     * Executes the todo command to add a todo task.
     * 
     * @param taskList The list of tasks to add the todo to.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        String description = Parser.parseTodoDescription(userInput);

        // Create and add the todo task
        Task task = new Todo(description);
        taskList.addTask(task);
        saveTasks(taskList, ui, storage);
        ui.printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + taskList.size() + " tasks in the list."
        );
    }
    
    private void saveTasks(TaskList taskList, Ui ui, Storage storage) {
        try {
            storage.save(new java.util.ArrayList<>(taskList.toFileFormat()));
        } catch (BuddyException e) {
            ui.showError("Error saving tasks: " + e.getMessage());
        }
    }
}