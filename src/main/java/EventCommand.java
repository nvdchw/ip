/**
 * Command to add an event task.
 */
public class EventCommand extends Command {
    private final String userInput;
    
    public EventCommand(String userInput) {
        this.userInput = userInput;
    }
    
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        String[] parts = Parser.parseEvent(userInput);
        String description = parts[0];
        String from = parts[1];
        String to = parts[2];

        try {
            // Create and add the event task
            Task task = new Event(description, from, to);
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