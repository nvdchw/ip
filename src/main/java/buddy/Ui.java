package buddy;

import java.util.Scanner;

/**
 * Ui handles all user interactions including input and output.
 */
public class Ui {
    private final Scanner scanner;
    private String lastOutput = "";

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
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        lastOutput = sb.toString();
        System.out.println(lastOutput);
    }

    public String getLastOutput() {
        return lastOutput;
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
        printBox("Hey there! I'm Buddy, your personal task companion!",
                "Ready to help you stay organised? What would you like to do?");

    }

    /**
     * Shows the goodbye message.
     */
    public void showGoodbye() {
        printBox("All done! Keep crushing those tasks! See you soon!");
    }

    /**
     * Shows an error message in a box.
     *
     * @param message the error message
     */
    public void showError(String message) {
        printBox("Oops! " + message, "Want to try that again?");
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}
