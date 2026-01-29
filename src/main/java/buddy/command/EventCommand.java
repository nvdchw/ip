package buddy.command;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.*;

/**
 * Command to add an event task.
 */
public class EventCommand extends Command {
    private final String userInput;
    
    /**
     * Constructs an EventCommand with the given user input.
     * 
     * @param userInput The full user input string for the event command.
     */
    public EventCommand(String userInput) {
        this.userInput = userInput;
    }
    
    /**
     * Executes the event command to add an event task.
     * 
     * @param taskList The list of tasks to add the event to.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
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