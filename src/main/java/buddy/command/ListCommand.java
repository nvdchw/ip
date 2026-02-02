package buddy.command;

import buddy.BuddyException;
import buddy.Storage;
import buddy.Ui;

import buddy.task.TaskList;

/**
 * Command to list all tasks.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command to display all tasks.
     * 
     * @param taskList The list of tasks to display.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler (not used in this command).
     * @throws BuddyException Not thrown in this command.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        // If no tasks, inform the user
        if (taskList.isEmpty()) {
            ui.printBox("No tasks yet. Add one to get started!");
            return;
        }

        // Prepare lines to print the task list
        String[] lines = new String[taskList.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < taskList.size(); i++) {
            lines[i + 1] = (i + 1) + "." + taskList.getTask(i);
        }
        ui.printBox(lines);
    }
}