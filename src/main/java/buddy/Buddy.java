package buddy;

import java.util.ArrayList;

import buddy.command.Command;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.task.TaskParser;

/**
 * Buddy is a simple command-line task management application.
 * It allows users to add, mark, unmark, delete, and list tasks.
 */
public class Buddy {

    private static final String DATA_FILE = "./data/buddy.txt";

    private final Ui ui;
    private final Storage storage;
    private String commandType;

    /**
     * Constructs a Buddy application instance.
     */
    public Buddy() {
        this.ui = new Ui();
        this.storage = new Storage(DATA_FILE);
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parseCommand(input);
            c.execute(loadTasks(), ui, storage);
            commandType = c.getClass().getSimpleName();
            String output = ui.getLastOutput();
            return output.isEmpty() ? c.toString() : output;
        } catch (BuddyException e) {
            commandType = "Error";
            return "Error: " + e.getMessage();
        }
    }
    public String getCommandType() {
        return commandType;
    }

    private TaskList loadTasks() {
        TaskList taskList = new TaskList();
        try {
            ArrayList<String> lines = new ArrayList<>(storage.load());
            for (String line : lines) {
                try {
                    Task task = TaskParser.parseFromFile(line);
                    taskList.addTask(task);
                } catch (BuddyException e) {
                    System.out.println("Warning: Skipping corrupted line: " + line);
                }
            }
        } catch (BuddyException e) {
            ui.showError("Error loading tasks: " + e.getMessage());
        }

        return taskList;
    }

    /**
     * Runs the Buddy application, handling user input and commands.
     */
    public void run() {
        TaskList taskList = loadTasks();
        ui.showWelcome();
        String userInput = ui.readCommand();

        while (true) {
            try {
                Command command = Parser.parseCommand(userInput);
                command.execute(taskList, ui, storage);
                if (command.isExit()) {
                    break;
                }
            } catch (BuddyException e) {
                ui.showError(e.getMessage());
            }

            userInput = ui.readCommand();
        }

        ui.close();
        ui.showGoodbye();
    }
    /**
     * Main method to run the Buddy application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Buddy().run();
    }
}
