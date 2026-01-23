import java.util.Scanner;
import java.util.ArrayList;

/**
 * Buddy is a simple command-line task management application.
 * It allows users to add, mark, unmark, delete, and list tasks.
 */
public class Buddy {

    // Enum for command types
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

    // Method to print messages in a formatted box
    private static void printBox(String... lines) {
        System.out.println("\t____________________________________________________________");
        for (String line : lines) {
            System.out.println("\t" + line);
        }
        System.out.println("\t____________________________________________________________");
    }

    // Handler for the 'list' command
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

    // Handler for the 'mark' command
    private static void handleMark(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(5).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Mark the task as done
            tasks.get(taskIndex).markAsDone();
            printBox(
                "Nice! I've marked this task as done:",
                "  " + tasks.get(taskIndex)
            );
        } catch (NumberFormatException e) {
            // Handle invalid number format
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    // Handler for the 'unmark' command
    private static void handleUnmark(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Mark the task as not done
            tasks.get(taskIndex).markAsUndone();
            printBox(
                "OK, I've marked this task as not done yet:",
                "  " + tasks.get(taskIndex)
            );
        } catch (NumberFormatException e) {
            // Handle invalid number format
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    // Handler for the 'delete' command
    private static void handleDelete(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Remove the task and inform the user
            Task deletedTask = tasks.remove(taskIndex);
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

    // Handler for the 'todo' command
    private static void handleTodo(ArrayList<Task> tasks, String userInput) throws BuddyException {
        String description = userInput.length() > 5 ? userInput.substring(5).trim() : "";
        
        // Check for empty description
        if (description.isEmpty()) {
            throw new BuddyException("A todo needs a description.");
        }

        // Create and add the todo task
        Task task = new Todo(description);
        tasks.add(task);
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    // Handler for the 'deadline' command
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
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    // Handler for the 'event' command
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
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    public static void main(String[] args) {
        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // List to store tasks
        ArrayList<Task> tasks = new ArrayList<>();

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
