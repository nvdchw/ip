package buddy;
/**
 * Command to add a deadline task.
 */
public class DeadlineCommand extends Command {
    private final String userInput;
    
    public DeadlineCommand(String userInput) {
        this.userInput = userInput;
    }
    
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        String[] parts = Parser.parseDeadline(userInput);
        String description = parts[0];
        String by = parts[1];

        try {
            // Create and add the deadline task
            Task task = new Deadline(description, by);
            taskList.addTask(task);
            saveTasks(taskList, ui, storage);
            ui.printBox(
                "Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskList.size() + " tasks in the list."
            );
        } catch (IllegalArgumentException e) {
            throw new BuddyException(e.getMessage());
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