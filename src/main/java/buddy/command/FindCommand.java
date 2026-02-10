package buddy.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.DateTimeUtil;
import buddy.task.Deadline;
import buddy.task.Event;
import buddy.task.Task;
import buddy.task.TaskList;


/**
 * Command to find tasks by date or keyword search.
 */
public class FindCommand extends Command {
    private final String userInput;
    /**
     * Constructs a FindCommand with the given user input.
     * @param userInput The full user input string for the find command.
     */
    public FindCommand(String userInput) {
        this.userInput = userInput;
    }
    /**
     * Executes the find command to search for tasks by date or keyword.
     * @param taskList The list of tasks to search through.
     * @param ui The user interface for displaying messages.
     * @param storage The storage handler (not used in this command).
     * @throws BuddyException If there is an error parsing the search criteria.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        String searchTerm = Parser.parseFindDate(userInput);

        // Try to parse as date first
        try {
            LocalDate searchDate = DateTimeUtil.parseFindDate(searchTerm);
            findByDate(taskList, ui, searchDate);
        } catch (DateTimeParseException e) {
            // If date parsing fails, search by keyword
            findByKeyword(taskList, ui, searchTerm);
        }
    }

    /**
     * Searches for tasks on a specific date.
     */
    private void findByDate(TaskList taskList, Ui ui, LocalDate searchDate) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        // Check Deadlines and Events for matching date
        for (Task task : taskList.getAllTasks()) {
            if (matchesDate(task, searchDate)) {
                matchingTasks.add(task);
            }
        }

        // Display results
        if (matchingTasks.isEmpty()) {
            ui.printBox("No tasks found on " + searchDate);
        } else {
            String[] lines = new String[matchingTasks.size() + 1];
            lines[0] = "Tasks on " + searchDate + ":";
            for (int i = 0; i < matchingTasks.size(); i++) {
                lines[i + 1] = (i + 1) + "." + matchingTasks.get(i);
            }
            ui.printBox(lines);
        }
    }

    /**
     * Searches for tasks matching a keyword in their description.
     */
    private void findByKeyword(TaskList taskList, Ui ui, String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        // Search tasks by keyword in description
        for (Task task : taskList.getAllTasks()) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }

        // Display results
        if (matchingTasks.isEmpty()) {
            ui.printBox("No tasks found matching: \"" + keyword + "\"");
        } else {
            String[] lines = new String[matchingTasks.size() + 1];
            lines[0] = "Tasks matching \"" + keyword + "\":";
            for (int i = 0; i < matchingTasks.size(); i++) {
                lines[i + 1] = (i + 1) + "." + matchingTasks.get(i);
            }
            ui.printBox(lines);
        }
    }

    private boolean matchesDate(Task task, LocalDate searchDate) {
        if (task instanceof Deadline deadline) {
            return deadline.getDateTime().toLocalDate().equals(searchDate);
        }
        if (task instanceof Event event) {
            return eventCoversDate(event, searchDate);
        }
        return false;
    }

    private boolean eventCoversDate(Event event, LocalDate searchDate) {
        LocalDate startDate = event.getStartTime().toLocalDate();
        LocalDate endDate = event.getEndTime().toLocalDate();
        return (startDate.isBefore(searchDate) || startDate.equals(searchDate))
                && (endDate.isAfter(searchDate) || endDate.equals(searchDate));
    }
}
