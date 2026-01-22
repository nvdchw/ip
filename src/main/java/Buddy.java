import java.util.Scanner;
import java.util.ArrayList;

public class Buddy {
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

    private static void handleMark(ArrayList<Task> commands, String userInput) {
        int taskIndex = Integer.parseInt(userInput.substring(5)) - 1;
        commands.get(taskIndex).markAsDone();
        printBox(
            "Nice! I've marked this task as done:",
            "  " + commands.get(taskIndex)
        );
    }

    private static void handleUnmark(ArrayList<Task> commands, String userInput) {
        int taskIndex = Integer.parseInt(userInput.substring(7)) - 1;
        commands.get(taskIndex).markAsUndone();
        printBox(
            "OK, I've marked this task as not done yet:",
            "  " + commands.get(taskIndex)
        );
    }

    private static void handleTodo(ArrayList<Task> commands, String userInput) {
        String description = userInput.substring(5);
        Task task = new Todo(description);
        commands.add(task);
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + commands.size() + " tasks in the list."
        );
    }

    private static void handleDeadline(ArrayList<Task> commands, String userInput) {
        String content = userInput.substring(9);
        int byIndex = content.indexOf(" /by ");
        String description = content.substring(0, byIndex);
        String by = content.substring(byIndex + 5);
        Task task = new Deadline(description, by);
        commands.add(task);
        printBox(
            "Got it. I've added this task:",
            "  " + task,
            "Now you have " + commands.size() + " tasks in the list."
        );
    }

    private static void handleEvent(ArrayList<Task> commands, String userInput) {
        String content = userInput.substring(6);
        int fromIndex = content.indexOf(" /from ");
        int toIndex = content.indexOf(" /to ");
        String description = content.substring(0, fromIndex);
        String from = content.substring(fromIndex + 7, toIndex);
        String to = content.substring(toIndex + 5);
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

        while (!userInput.equals("bye")) {
            if (userInput.equals("list")) {
                handleList(commands);
            } else if (userInput.startsWith("mark ")) {
                handleMark(commands, userInput);
            } else if (userInput.startsWith("unmark ")) {
                handleUnmark(commands, userInput);
            } else if (userInput.startsWith("todo ")) {
                handleTodo(commands, userInput);
            } else if (userInput.startsWith("deadline ")) {
                handleDeadline(commands, userInput);
            } else if (userInput.startsWith("event ")) {
                handleEvent(commands, userInput);
            } else {
                printBox("I don't recognize that command.");
            }
            userInput = scanner.nextLine();
        }
        scanner.close();
        printBox("Bye. Hope to see you again soon!");
    }
}
