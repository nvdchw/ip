package buddy.command;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.Event;
import buddy.task.TaskList;


/**
 * Command to add an event task.
 */
public class EventCommand extends Command {
    private final String userInput;
    /**
     * Constructs an EventCommand with the given user input.
     * @param userInput The full user input string for the event command.
     */
    public EventCommand(String userInput) {
        this.userInput = userInput;
    }
    /**
     * Executes the event command to add an event task.
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
        String tag = parts[3];

        addAndReport(() -> new Event(description, from, to, tag), taskList, ui, storage);
    }
}
