import java.util.Scanner;
import java.util.ArrayList;

public class Buddy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> commands = new ArrayList<>();
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
                for (int i = 0; i < commands.size(); i++) {
                    System.out.println(i+1 + ". " + commands.get(i));
                }
                System.out.println("    ____________________________________________________________");
            } else {
                System.out.println("    ____________________________________________________________");
                System.out.println("    added: " + userInput);
                System.out.println("    ____________________________________________________________");
                commands.add(userInput);
            }
            userInput = scanner.nextLine();
        }

        System.out.println("    ____________________________________________________________");
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println("    ____________________________________________________________");
    }
}
