package buddy.command;

import buddy.BuddyException;
import buddy.Storage;
import buddy.Ui;
import buddy.task.TaskList;


/**
 * Command to exit the application.
 */
public class ByeCommand extends Command {
    /**
     * Executes the bye command which signals the application to exit.
     * @param taskList The list of tasks (not used in this command).
     * @param ui The user interface (not used in this command).
     * @param storage The storage handler (not used in this command).
     * @throws BuddyException Not thrown in this command.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        // No action needed - exit is handled by isExit() method
    }

    /**
     * Indicates that this command signals the application to exit.
     * @return true since this command is for exiting the application.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
