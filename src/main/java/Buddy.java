import java.util.Scanner;
import java.util.ArrayList;

public class Buddy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> commands = new ArrayList<>();
        String logo = "    ____  _    _ _____  _______     __\n" +
                "   |  _ \\| |  | |  __ \\|  __ \\ \\   / /\n" +
                "   | |_) | |  | | |  | | |  | \\ \\_/ / \n" +
                "   |  _ <| |  | | |  | | |  | |\\   /  \n" +
                "   | |_) | |__| | |__| | |__| | | |   \n" +
                "   |____/ \\____/|_____/|_____/  |_|   \n";
        System.out.println(logo);
        System.out.println("    ____________________________________________________________");
        System.out.println("    Hello I'm Buddy!");
        System.out.println("    What can I do for you?");
        System.out.println("    ____________________________________________________________");
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {
            if (userInput.equals("list")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    Here are the tasks in your list:");
                for (int i = 0; i < commands.size(); i++) {
                    System.out.println("    " + (i+1) + "." + commands.get(i));
                }
                System.out.println("    ____________________________________________________________");
            } else if (userInput.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(userInput.substring(5)) - 1;
                commands.get(taskIndex).markAsDone();
                System.out.println("    ____________________________________________________________");
                System.out.println("    Nice! I've marked this task as done:");
                System.out.println("      " + commands.get(taskIndex));
                System.out.println("    ____________________________________________________________");
            } else if (userInput.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(userInput.substring(7)) - 1;
                commands.get(taskIndex).markAsUndone();
                System.out.println("    ____________________________________________________________");
                System.out.println("    OK, I've marked this task as not done yet:");
                System.out.println("      " + commands.get(taskIndex));
                System.out.println("    ____________________________________________________________");
            } else if (userInput.startsWith("todo ")) {
                String description = userInput.substring(5);
                Task task = new Todo(description);
                commands.add(task);
                System.out.println("    ____________________________________________________________");
                System.out.println("    Got it. I've added this task:");
                System.out.println("      " + task);
                System.out.println("    Now you have " + commands.size() + " tasks in the list.");
                System.out.println("    ____________________________________________________________");
            } else if (userInput.startsWith("deadline ")) {
                String content = userInput.substring(9);
                int byIndex = content.indexOf(" /by ");
                String description = content.substring(0, byIndex);
                String by = content.substring(byIndex + 5);
                Task task = new Deadline(description, by);
                commands.add(task);
                System.out.println("    ____________________________________________________________");
                System.out.println("    Got it. I've added this task:");
                System.out.println("      " + task);
                System.out.println("    Now you have " + commands.size() + " tasks in the list.");
                System.out.println("    ____________________________________________________________");
            } else if (userInput.startsWith("event ")) {
                String content = userInput.substring(6);
                int fromIndex = content.indexOf(" /from ");
                int toIndex = content.indexOf(" /to ");
                String description = content.substring(0, fromIndex);
                String from = content.substring(fromIndex + 7, toIndex);
                String to = content.substring(toIndex + 5);
                Task task = new Event(description, from, to);
                commands.add(task);
                System.out.println("    ____________________________________________________________");
                System.out.println("    Got it. I've added this task:");
                System.out.println("      " + task);
                System.out.println("    Now you have " + commands.size() + " tasks in the list.");
                System.out.println("    ____________________________________________________________");
            }
            userInput = scanner.nextLine();
        }
        scanner.close();
        System.out.println("    ____________________________________________________________");
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println("    ____________________________________________________________");
    }
}
