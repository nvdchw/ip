package buddy.command;

import buddy.BuddyException;
import buddy.Constants;
import buddy.Parser;
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
     * 
     * @param userInput The full user input string for the mark command.
     */
    public MarkCommand(String userInput) {
        this.userInput = userInput;
    }
    
    /**
     * Executes the mark command to mark a task as done.
     * 
     * @param taskList The list of tasks to mark from.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        try {
            int taskIndex = Parser.parseTaskNumber(userInput, Constants.MARK_LENGTH);
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Mark the task as done
            taskList.getTask(taskIndex).markAsDone();
            saveTasks(taskList, ui, storage);
            ui.printBox(
                "Nice! I've marked this task as done:",
                "  " + taskList.getTask(taskIndex)
            );
        } catch (NumberFormatException e) {
            throw new BuddyException("Please provide a valid task number.");
        }
    }
    
    private void saveTasks(TaskList taskList, Ui ui, Storage storage) {
        try {
            storage.save(new java.util.ArrayList<>(taskList.toFileFormat()));
        } catch (BuddyException e) {
            ui.showError("Error saving tasks: " + e.getMessage());
        }
    }
}