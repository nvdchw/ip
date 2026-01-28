/**
 * Command to exit the application.
 */
public class ByeCommand extends Command {
    
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        // No action needed - exit is handled by isExit() method
    }
    
    @Override
    public boolean isExit() {
        return true;
    }
}