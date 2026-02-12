package buddy.command;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.Deadline;
import buddy.task.TaskList;


/**
 * Command to add a deadline task.
 */
public class DeadlineCommand extends Command {
    private final String userInput;
    /**
     * Constructs a DeadlineCommand with the given user input.
     * @param userInput The full user input string for the deadline command.
     */
    public DeadlineCommand(String userInput) {
        this.userInput = userInput;
    }
    /**
     * Executes the deadline command to add a deadline task.
     * @param taskList The list of tasks to add the deadline to.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler for saving tasks.
     * @throws BuddyException If there is an error parsing the input or saving the task.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        String[] parts = Parser.parseDeadline(userInput);
        String description = parts[0];
        String by = parts[1];
        String tag = parts[2];

        addAndReport(() -> new Deadline(description, by, tag), taskList, ui, storage);
    }
}
