package buddy;

import buddy.task.TaskList;

/**
 * Command to list all tasks.
 */
public class ListCommand extends Command {
    
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