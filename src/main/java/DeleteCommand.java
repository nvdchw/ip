/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private final String userInput;
    
    public DeleteCommand(String userInput) {
        this.userInput = userInput;
    }
    
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        try {
            int taskIndex = Parser.parseTaskNumber(userInput, 7);
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= taskList.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Remove the task and inform the user
            Task deletedTask = taskList.removeTask(taskIndex);
            saveTasks(taskList, ui, storage);
            ui.printBox(
                "Noted. I've removed this task:",
                "  " + deletedTask,
                "Now you have " + taskList.size() + " tasks in the list."
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