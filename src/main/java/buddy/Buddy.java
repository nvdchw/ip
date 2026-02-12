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
    private final TaskList taskList;
    private CommandType commandType;

    /**
     * Constructs a Buddy application instance.
     */
    public Buddy() {
        this.ui = new Ui();
        this.storage = new Storage(DATA_FILE);
        this.taskList = loadTasks();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Command command = executeCommand(input);
            commandType = CommandType.fromCommand(command);
            String output = ui.getLastOutput();
            return output.isEmpty() ? command.toString() : output;
        } catch (BuddyException e) {
            commandType = CommandType.ERROR;
            return "Error: " + e.getMessage();
        }
    }
    public CommandType getCommandType() {
        return commandType;
    }

    private TaskList loadTasks() {
        ArrayList<String> lines = loadLines();
        return parseTasks(lines);
    }

    private ArrayList<String> loadLines() {
        try {
            return new ArrayList<>(storage.load());
        } catch (BuddyException e) {
            ui.showError("Error loading tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private TaskList parseTasks(ArrayList<String> lines) {
        TaskList taskList = new TaskList();
        for (String line : lines) {
            try {
                Task task = TaskParser.parseFromFile(line);
                taskList.addTask(task);
            } catch (BuddyException e) {
                System.out.println("Warning: Skipping corrupted line: " + line);
            }
        }
        return taskList;
    }

    /**
     * Runs the Buddy application, handling user input and commands.
     */
    public void run() {
        ui.showWelcome();
        String userInput = ui.readCommand();
        handleUserInputs(userInput);

        ui.close();
        ui.showGoodbye();
    }

    private void handleUserInputs(String userInput) {
        while (true) {
            try {
                Command command = executeCommand(userInput);
                if (command.isExit()) {
                    break;
                }
            } catch (BuddyException e) {
                ui.showError(e.getMessage());
            }

            userInput = ui.readCommand();
        }
    }

    private Command executeCommand(String input) throws BuddyException {
        Command command = Parser.parseCommand(input);
        command.execute(taskList, ui, storage);
        return command;
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
