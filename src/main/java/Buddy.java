import java.util.Scanner;
import java.util.ArrayList;

public class Buddy {
    private static void handleList(ArrayList<Task> commands) {
        System.out.println("\t____________________________________________________________");
        System.out.println("\tHere are the tasks in your list:");
        for (int i = 0; i < commands.size(); i++) {
            System.out.println("\t" + (i + 1) + "." + commands.get(i));
        }
        System.out.println("\t____________________________________________________________");
    }

    private static void handleMark(ArrayList<Task> commands, String userInput) {
        int taskIndex = Integer.parseInt(userInput.substring(5)) - 1;
        commands.get(taskIndex).markAsDone();
        System.out.println("\t____________________________________________________________");
        System.out.println("\tNice! I've marked this task as done:");
        System.out.println("\t  " + commands.get(taskIndex));
        System.out.println("\t____________________________________________________________");
    }

    private static void handleUnmark(ArrayList<Task> commands, String userInput) {
        int taskIndex = Integer.parseInt(userInput.substring(7)) - 1;
        commands.get(taskIndex).markAsUndone();
        System.out.println("\t____________________________________________________________");
        System.out.println("\tOK, I've marked this task as not done yet:");
        System.out.println("\t  " + commands.get(taskIndex));
        System.out.println("\t____________________________________________________________");
    }

    private static void handleTodo(ArrayList<Task> commands, String userInput) {
        String description = userInput.substring(5);
        Task task = new Todo(description);
        commands.add(task);
        System.out.println("\t____________________________________________________________");
        System.out.println("\tGot it. I've added this task:");
        System.out.println("\t  " + task);
        System.out.println("\tNow you have " + commands.size() + " tasks in the list.");
        System.out.println("\t____________________________________________________________");
    }

    private static void handleDeadline(ArrayList<Task> commands, String userInput) {
        String content = userInput.substring(9);
        int byIndex = content.indexOf(" /by ");
        String description = content.substring(0, byIndex);
        String by = content.substring(byIndex + 5);
        Task task = new Deadline(description, by);
        commands.add(task);
        System.out.println("\t____________________________________________________________");
        System.out.println("\tGot it. I've added this task:");
        System.out.println("\t  " + task);
        System.out.println("\tNow you have " + commands.size() + " tasks in the list.");
        System.out.println("\t____________________________________________________________");
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
        System.out.println("\t____________________________________________________________");
        System.out.println("\tGot it. I've added this task:");
        System.out.println("\t  " + task);
        System.out.println("\tNow you have " + commands.size() + " tasks in the list.");
        System.out.println("\t____________________________________________________________");
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
        System.out.println("\t____________________________________________________________");
        System.out.println("\tHello I'm Buddy!");
        System.out.println("\tWhat can I do for you?");
        System.out.println("\t____________________________________________________________");
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
            }
            userInput = scanner.nextLine();
        }
        scanner.close();
        System.out.println("    ____________________________________________________________");
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println("    ____________________________________________________________");
    }
}
