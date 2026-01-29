package buddy.command;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import buddy.BuddyException;
import buddy.Parser;
import buddy.Storage;
import buddy.Ui;
import buddy.task.*;

/**
 * Command to find tasks on a specific date.
 */
public class FindCommand extends Command {
    private final String userInput;
    
    public FindCommand(String userInput) {
        this.userInput = userInput;
    }
    
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws BuddyException {
        String dateString = Parser.parseFindDate(userInput);

        try {
            LocalDate searchDate = LocalDate.parse(dateString, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            ArrayList<Task> matchingTasks = new ArrayList<>();
            for (Task task : taskList.getAllTasks()) {
                if (task instanceof Deadline deadline) {
                    if (deadline.getDateTime().toLocalDate().equals(searchDate)) {
                        matchingTasks.add(task);
                    }
                } else if (task instanceof Event event) {
                    LocalDate startDate = event.getStartTime().toLocalDate();
                    LocalDate endDate = event.getEndTime().toLocalDate();
                    if ((startDate.isBefore(searchDate) || startDate.equals(searchDate)) &&
                        (endDate.isAfter(searchDate) || endDate.equals(searchDate))) {
                        matchingTasks.add(task);
                    }
                }
            }

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
        } catch (Exception e) {
            throw new BuddyException("Invalid date format. Use yyyy-MM-dd (e.g., 2026-01-02)");
        }
    }
}