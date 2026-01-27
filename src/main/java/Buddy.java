import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Buddy is a simple command-line task management application.
 * It allows users to add, mark, unmark, delete, and list tasks.
 */
public class Buddy {

    // Constants for data storage
    private static final String DATA_DIR = "./data";
    private static final String DATA_FILE = "./data/buddy.txt";

    /**
     * Enum for command types.
     */
    private enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE, UNKNOWN;

        // Method to parse user input into Command enum
        static Command fromUserInput(String input) {
            String trimmed = input.trim();
            int space = trimmed.indexOf(' ');
            String keyword = (space == -1) ? trimmed : trimmed.substring(0, space);
            return switch (keyword) {
            case "list" -> LIST;
            case "mark" -> MARK;
            case "unmark" -> UNMARK;
            case "todo" -> TODO;
            case "deadline" -> DEADLINE;
            case "event" -> EVENT;
            case "delete" -> DELETE;
            case "bye" -> BYE;
            default -> UNKNOWN;
            };
        }
    }

    /**
     * Prints messages in a boxed format.
     *
     * @param lines the lines of text to print inside the box
     */
    private static void printBox(String... lines) {
        System.out.println("\t____________________________________________________________");
        for (String line : lines) {
            System.out.println("\t" + line);
        }
        System.out.println("\t____________________________________________________________");
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks the list of tasks to save
     */
    private static void saveTasks(ArrayList<Task> tasks) {
        try {
            // Create directory if it does not exist yet
            File directory = new File(DATA_DIR);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.out.println("Error: Failed to create data directory.");
                    return;
                }
            }

            // Write tasks to file
            FileWriter writer = new FileWriter(DATA_FILE);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the data file.
     *
     * @return the list of loaded tasks
     */
    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Path filePath = Paths.get(DATA_FILE);

        // Check if file exists
        if (!Files.exists(filePath)) {
            return tasks; // Return empty list if file doesn't exist
        }

        try {
            // Read all lines from file
            for (String line : Files.readAllLines(filePath)) {
                try {
                    Task task = parseTaskFromFile(line);
                    tasks.add(task);
                } catch (Exception e) {
                    // Skip corrupted lines
                    System.out.println("Warning: Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Parses a task from a file format string.
     *
     * @param line the line from the file
     * @return the parsed task
     * @throws BuddyException if the line format is invalid
     */
    private static Task parseTaskFromFile(String line) throws BuddyException {
        // Check for valid format
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new BuddyException("Invalid file format");
        }

        // Extract common fields
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        // Create task based on type
        Task task = switch (type) {
        case "T" -> new Todo(description);
        case "D" -> {
            if (parts.length < 4) throw new BuddyException("Invalid deadline format");
            yield new Deadline(description, parts[3]);
        }
        case "E" -> {
            if (parts.length < 5) throw new BuddyException("Invalid event format");
            yield new Event(description, parts[3], parts[4]);
        }
        default -> throw new BuddyException("Unknown task type: " + type);
        };

        // Set completion status
        if (isDone) {
            task.markAsDone();
        }

        return task;
    }


    /**
     * Handler for the 'list' command.
     *
     * @param tasks the list of tasks
     */
    private static void handleList(ArrayList<Task> tasks) {
        // If no tasks, inform the user
        if (tasks.isEmpty()) {
            printBox("No tasks yet. Add one to get started!");
            return;
        }

        // Prepare lines to print the task list
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }
        printBox(lines);
    }

    /**
     * Handler for the 'mark' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the task number is invalid
     */
    private static void handleMark(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(5).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Mark the task as done
            tasks.get(taskIndex).markAsDone();
            saveTasks(tasks); // Save to file after marking
            printBox(
                "Nice! I've marked this task as done:",
                "  " + tasks.get(taskIndex)
            );
        } catch (NumberFormatException e) {
            // Handle invalid number format
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    /**
     * Handler for the 'unmark' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the task number is invalid
     */
    private static void handleUnmark(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Mark the task as not done
            tasks.get(taskIndex).markAsUndone();
            saveTasks(tasks); // Save to file after unmarking
            printBox(
                "OK, I've marked this task as not done yet:",
                "  " + tasks.get(taskIndex)
            );
        } catch (NumberFormatException e) {
            // Handle invalid number format
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    /**
     * Handler for the 'delete' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the task number is invalid
     */
    private static void handleDelete(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Remove the task and inform the user
            Task deletedTask = tasks.remove(taskIndex);
            saveTasks(tasks); // Save to file after deletion
            printBox(
                "Noted. I've removed this task:",
                "  " + deletedTask,
                "Now you have " + tasks.size() + " tasks in the list."
            );
        } catch (NumberFormatException e) {
            // Handle invalid number format
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    /**
     * Handler for the 'todo' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the description is empty
     */
    private static void handleTodo(ArrayList<Task> tasks, String userInput) throws BuddyException {
        String description = userInput.length() > 5 ? userInput.substring(5).trim() : "";
        
        // Check for empty description
        if (description.isEmpty()) {
            throw new BuddyException("A todo needs a description.");
        }

        // Create and add the todo task
        Task task = new Todo(description);
        tasks.add(task);
        saveTasks(tasks); // Save to file after adding todo task
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }    

    /**
     * Handler for the 'deadline' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the description or time is invalid
     */
    private static void handleDeadline(ArrayList<Task> tasks, String userInput) throws BuddyException {
        String content = userInput.length() > 9 ? userInput.substring(9).trim() : "";
        int byIndex = content.indexOf(" /by ");
        
        // Check for valid format
        if (content.isEmpty() || byIndex == -1) {
            throw new BuddyException("Deadline format: deadline <desc> /by <time>");
        }

        String description = content.substring(0, byIndex).trim();
        String by = content.substring(byIndex + 5).trim();

        // Create and add the deadline task
        Task task = new Deadline(description, by);
        tasks.add(task);
        saveTasks(tasks); // Save to file after adding deadline task
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    /**
     * Handler for the 'event' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the description, start time, or end time is invalid
     */
    private static void handleEvent(ArrayList<Task> tasks, String userInput) throws BuddyException {
        String content = userInput.length() > 6 ? userInput.substring(6).trim() : "";
        int fromIndex = content.indexOf(" /from ");
        int toIndex = content.indexOf(" /to ");

        // Check for valid format
        if (content.isEmpty() || fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BuddyException("Event format: event <desc> /from <start> /to <end>");
        }

        String description = content.substring(0, fromIndex).trim();
        String from = content.substring(fromIndex + 7, toIndex).trim();
        String to = content.substring(toIndex + 5).trim();

        // Check for empty fields
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new BuddyException("Event needs description, start, and end time.");
        }

        // Create and add the event task
        Task task = new Event(description, from, to);
        tasks.add(task);
        saveTasks(tasks); // Save to file after adding event task
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    
    /**
     * Main method to run the Buddy application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // List to store tasks
        ArrayList<Task> tasks = loadTasks();

        // Print welcome message
        String logo = "\t____  _    _ _____  _______     __\n" +
                "   |  _ \\| |  | |  __ \\|  __ \\ \\   / /\n" +
                "   | |_) | |  | | |  | | |  | \\ \\_/ / \n" +
                "   |  _ <| |  | | |  | | |  | |\\   /  \n" +
                "   | |_) | |__| | |__| | |__| | | |   \n" +
                "   |____/ \\____/|_____/|_____/  |_|   \n";
        System.out.println(logo);
        printBox("Hello I'm Buddy!", "What can I do for you?");
        
        // Read the first user input
        String userInput = scanner.nextLine();

        // Main command processing loop
        while (true) {
            // Parse the command to obtain the command enum
            Command cmd = Command.fromUserInput(userInput);

            // Exit if command is BYE
            if (cmd == Command.BYE) {
                break;
            }
            
            // Handle tasks with exception handling
            try {
                switch (cmd) {
                case LIST -> handleList(tasks);
                case MARK -> handleMark(tasks, userInput);
                case UNMARK -> handleUnmark(tasks, userInput);
                case DELETE -> handleDelete(tasks, userInput);
                case TODO -> handleTodo(tasks, userInput);
                case DEADLINE -> handleDeadline(tasks, userInput);
                case EVENT -> handleEvent(tasks, userInput);
                default -> throw new BuddyException("I don't recognize that command.");
                }
            } catch (BuddyException e) {
                printBox("Oh No! " + e.getMessage());
            }

            // Read the next user input
            userInput = scanner.nextLine();
        }

        // Close the scanner and print goodbye message
        scanner.close();
        printBox("Bye. Hope to see you again soon!");
    }
}
