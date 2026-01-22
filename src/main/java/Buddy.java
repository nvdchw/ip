import java.util.Scanner;
import java.util.ArrayList;

public class Buddy {

    // Enum for command types
    private enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE, UNKNOWN;

        // Method to parse user input into Command enum
        static Command fromUserInput(String input) {
            String trimmed = input.trim();
            int space = trimmed.indexOf(' ');
            String keyword = (space == -1) ? trimmed : trimmed.substring(0, space);
            switch (keyword) {
                case "list": return LIST;
                case "mark": return MARK;
                case "unmark": return UNMARK;
                case "todo": return TODO;
                case "deadline": return DEADLINE;
                case "event": return EVENT;
                case "delete": return DELETE;
                case "bye": return BYE;
                default: return UNKNOWN;
            }
        }
    }

    private static void printBox(String... lines) {
        System.out.println("\t____________________________________________________________");
        for (String line : lines) {
            System.out.println("\t" + line);
        }
        System.out.println("\t____________________________________________________________");
    }

    private static void handleList(ArrayList<Task> commands) {
        if (commands.isEmpty()) {
            printBox("No tasks yet. Add one to get started!");
            return;
        }
        String[] lines = new String[commands.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < commands.size(); i++) {
            lines[i + 1] = (i + 1) + "." + commands.get(i);
        }
        printBox(lines);
    }

    private static void handleMark(ArrayList<Task> commands, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(5).trim()) - 1;
            if (taskIndex < 0 || taskIndex >= commands.size()) {
                throw new BuddyException("Task number does not exist.");
            }
            commands.get(taskIndex).markAsDone();
            printBox(
                "Nice! I've marked this task as done:",
                "  " + commands.get(taskIndex)
            );
        } catch (NumberFormatException e) {
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    private static void handleUnmark(ArrayList<Task> commands, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            if (taskIndex < 0 || taskIndex >= commands.size()) {
                throw new BuddyException("Task number does not exist.");
            }
            commands.get(taskIndex).markAsUndone();
            printBox(
                "OK, I've marked this task as not done yet:",
                "  " + commands.get(taskIndex)
            );
        } catch (NumberFormatException e) {
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    private static void handleDelete(ArrayList<Task> commands, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            if (taskIndex < 0 || taskIndex >= commands.size()) {
                throw new BuddyException("Task number does not exist.");
            }
            Task deletedTask = commands.remove(taskIndex);
            printBox(
                "Noted. I've removed this task:",
                "  " + deletedTask,
                "Now you have " + commands.size() + " tasks in the list."
            );
        } catch (NumberFormatException e) {
            throw new BuddyException("Please provide a valid task number.");
        }
    }

    private static void handleTodo(ArrayList<Task> commands, String userInput) throws BuddyException {
        String description = userInput.length() > 5 ? userInput.substring(5).trim() : "";
        if (description.isEmpty()) {
            throw new BuddyException("A todo needs a description.");
        }
        Task task = new Todo(description);
        commands.add(task);
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + commands.size() + " tasks in the list."
        );
    }

    private static void handleDeadline(ArrayList<Task> commands, String userInput) throws BuddyException {
        String content = userInput.length() > 9 ? userInput.substring(9).trim() : "";
        int byIndex = content.indexOf(" /by ");
        if (content.isEmpty() || byIndex == -1) {
            throw new BuddyException("Deadline format: deadline <desc> /by <time>");
        }
        String description = content.substring(0, byIndex).trim();
        String by = content.substring(byIndex + 5).trim();
        Task task = new Deadline(description, by);
        commands.add(task);
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + commands.size() + " tasks in the list."
        );
    }

    private static void handleEvent(ArrayList<Task> commands, String userInput) throws BuddyException {
        String content = userInput.length() > 6 ? userInput.substring(6).trim() : "";
        int fromIndex = content.indexOf(" /from ");
        int toIndex = content.indexOf(" /to ");
        if (content.isEmpty() || fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BuddyException("Event format: event <desc> /from <start> /to <end>");
        }
        String description = content.substring(0, fromIndex).trim();
        String from = content.substring(fromIndex + 7, toIndex).trim();
        String to = content.substring(toIndex + 5).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new BuddyException("Event needs description, start, and end time.");
        }
        Task task = new Event(description, from, to);
        commands.add(task);
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + commands.size() + " tasks in the list."
        );
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> commands = new ArrayList<>();
        String logo = "\t____  _    _ _____  _______     __\n" +
                "   |  _ \\| |  | |  __ \\|  __ \\ \\   / /\n" +
                "   | |_) | |  | | |  | | |  | \\ \\_/ / \n" +
                "   |  _ <| |  | | |  | | |  | |\\   /  \n" +
                "   | |_) | |__| | |__| | |__| | | |   \n" +
                "   |____/ \\____/|_____/|_____/  |_|   \n";
        System.out.println(logo);
        printBox("Hello I'm Buddy!", "What can I do for you?");
        String userInput = scanner.nextLine();

        while (true) {
            Command cmd = Command.fromUserInput(userInput);
            if (cmd == Command.BYE) break;
            try {
                switch (cmd) {
                    case LIST:
                        handleList(commands);
                        break;
                    case MARK:
                        handleMark(commands, userInput);
                        break;
                    case UNMARK:
                        handleUnmark(commands, userInput);
                        break;
                    case DELETE:
                        handleDelete(commands, userInput);
                        break;
                    case TODO:
                        handleTodo(commands, userInput);
                        break;
                    case DEADLINE:
                        handleDeadline(commands, userInput);
                        break;
                    case EVENT:
                        handleEvent(commands, userInput);
                        break;
                    default:
                        throw new BuddyException("I don't recognize that command.");
                }
            } catch (BuddyException e) {
                printBox("Oh No! " + e.getMessage());
            }
            userInput = scanner.nextLine();
        }
        scanner.close();
        printBox("Bye. Hope to see you again soon!");
    }
}
