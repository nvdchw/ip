/**
 * Command to add a todo task.
 */
public class TodoCommand extends Command {
    private final String userInput;
    
    public TodoCommand(String userInput) {
        this.userInput = userInput;
    }
    
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