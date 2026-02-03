package buddy;

import java.util.Scanner;

/**
 * Ui handles all user interactions including input and output.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a Ui instance.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints messages in a boxed format.
     *
     * @param lines the lines of text to print inside the box
     */
    public void printBox(String... lines) {
        System.out.println("\t____________________________________________________________");
        for (String line : lines) {
            System.out.println("\t" + line);
        }
        System.out.println("\t____________________________________________________________");
    }

    /**
     * Reads a command from the user.
     *
     * @return the user input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the welcome message with logo.
     */
    public void showWelcome() {
        String logo = """
                \t____  _    _ _____  _______     __
                   |  _ \\| |  | |  __ \\|  __ \\ \\   / /
                   | |_) | |  | | |  | | |  | \\ \\_/ /
                   |  _ <| |  | | |  | | |  | |\\   /
                   | |_) | |__| | |__| | |__| | | |
                   |____/ \\____/|_____/|_____/  |_|
                """;
        System.out.println(logo);
        printBox("Hello I'm Buddy!", "What can I do for you?");
    }

    /**
     * Shows the goodbye message.
     */
    public void showGoodbye() {
        printBox("Bye. Hope to see you again soon!");
    }

    /**
     * Shows an error message in a box.
     *
     * @param message the error message
     */
    public void showError(String message) {
        printBox("Oh No! " + message);
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}
