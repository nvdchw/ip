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
            } else {
                System.out.println("    ____________________________________________________________");
                System.out.println("    added: " + userInput);
                System.out.println("    ____________________________________________________________");
                commands.add(new Task(userInput));
            }
            userInput = scanner.nextLine();
        }
        scanner.close();
        System.out.println("    ____________________________________________________________");
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println("    ____________________________________________________________");
    }
}
