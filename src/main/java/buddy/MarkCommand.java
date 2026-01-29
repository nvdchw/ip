package buddy;

import buddy.task.TaskList;

/**
 * Command to mark a task as done.
 */
public class MarkCommand extends Command {
    private final String userInput;
    
    public MarkCommand(String userInput) {
        this.userInput = userInput;
    }
    
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        try {
            int taskIndex = Parser.parseTaskNumber(userInput, 4);
            
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