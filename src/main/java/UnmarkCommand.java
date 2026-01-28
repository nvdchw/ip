/**
 * Command to unmark a task (mark as not done).
 */
public class UnmarkCommand extends Command {
    private final String userInput;
    
    public UnmarkCommand(String userInput) {
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

            // Mark the task as not done
            taskList.getTask(taskIndex).markAsUndone();
            saveTasks(taskList, ui, storage);
            ui.printBox(
                "OK, I've marked this task as not done yet:",
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