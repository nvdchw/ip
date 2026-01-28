import java.util.ArrayList;

/**
 * Buddy is a simple command-line task management application.
 * It allows users to add, mark, unmark, delete, and list tasks.
 */
public class Buddy {

    private static final String DATA_FILE = "./data/buddy.txt";

    private final Ui ui;
    private final Storage storage;

    public Buddy() {
        this.ui = new Ui();
        this.storage = new Storage(DATA_FILE);
    }

    /**
     * Enum for command types.
     */
    private enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, BYE, UNKNOWN;

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
            case "find" -> FIND;
            case "bye" -> BYE;
            default -> UNKNOWN;
            };
        }
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks the list of tasks to save
     */
    private void saveTasks(ArrayList<Task> tasks) {
        try {
            ArrayList<String> taskStrings = new ArrayList<>();
            for (Task task : tasks) {
                taskStrings.add(task.toFileFormat());
            }
            storage.save(taskStrings);
        } catch (BuddyException e) {
            ui.showError("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the data file.
     *
     * @return the list of loaded tasks
     */
    private ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        
        try {
            ArrayList<String> lines = new ArrayList<>(storage.load());
            for (String line : lines) {
                try {
                    Task task = parseTaskFromFile(line);
                    tasks.add(task);
                } catch (BuddyException e) {
                    // Skip corrupted lines
                    System.out.println("Warning: Skipping corrupted line: " + line);
                }
            }
        } catch (BuddyException e) {
            ui.showError("Error loading tasks: " + e.getMessage());
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
    private void handleList(ArrayList<Task> tasks) {
        // If no tasks, inform the user
        if (tasks.isEmpty()) {
            ui.printBox("No tasks yet. Add one to get started!");
            return;
        }

        // Prepare lines to print the task list
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }
        ui.printBox(lines);
    }

    /**
     * Handler for the 'mark' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the task number is invalid
     */
    private void handleMark(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(5).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Mark the task as done
            tasks.get(taskIndex).markAsDone();
            saveTasks(tasks); // Save to file after marking
            ui.printBox(
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
    private void handleUnmark(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Mark the task as not done
            tasks.get(taskIndex).markAsUndone();
            saveTasks(tasks); // Save to file after unmarking
            ui.printBox(
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
    private void handleDelete(ArrayList<Task> tasks, String userInput) throws BuddyException {
        try {
            int taskIndex = Integer.parseInt(userInput.substring(7).trim()) - 1;
            
            // Check for valid task index
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new BuddyException("Task number does not exist.");
            }

            // Remove the task and inform the user
            Task deletedTask = tasks.remove(taskIndex);
            saveTasks(tasks); // Save to file after deletion
            ui.printBox(
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
     * Handler for the 'find' command to find tasks on a specific date.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the date format is invalid
     */
    private void handleFind(ArrayList<Task> tasks, String userInput) throws BuddyException {
        String dateStr = userInput.length() > 5 ? userInput.substring(5).trim() : "";
        
        if (dateStr.isEmpty()) {
            throw new BuddyException("Find format: find <date> (yyyy-MM-dd)");
        }

        try {
            java.time.LocalDate searchDate = java.time.LocalDate.parse(dateStr, 
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            java.util.ArrayList<Task> matchingTasks = new java.util.ArrayList<>();
            for (Task task : tasks) {
                if (task instanceof Deadline deadline) {
                    if (deadline.getDateTime().toLocalDate().equals(searchDate)) {
                        matchingTasks.add(task);
                    }
                } else if (task instanceof Event event) {
                    java.time.LocalDate startDate = event.getStartTime().toLocalDate();
                    java.time.LocalDate endDate = event.getEndTime().toLocalDate();
                    if ((startDate.isBefore(searchDate) || startDate.equals(searchDate)) &&
                        (endDate.isAfter(searchDate) || endDate.equals(searchDate))) {
                        matchingTasks.add(task);
                    }
                }
            }

            if (matchingTasks.isEmpty()) {
                ui.printBox("No tasks found on " + searchDate);
            } else {
                String[] lines = new String[matchingTasks.size() + 1];
                lines[0] = "Tasks on " + searchDate + ":";
                for (int i = 0; i < matchingTasks.size(); i++) {
                    lines[i + 1] = (i + 1) + "." + matchingTasks.get(i);
                }
                ui.printBox(lines);
            }
        } catch (Exception e) {
            throw new BuddyException("Invalid date format. Use yyyy-MM-dd (e.g., 2026-01-02)");
        }
    }

    /**
     * Handler for the 'todo' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the description is empty
     */
    private void handleTodo(ArrayList<Task> tasks, String userInput) throws BuddyException {
        String description = userInput.length() > 5 ? userInput.substring(5).trim() : "";
        
        // Check for empty description
        if (description.isEmpty()) {
            throw new BuddyException("A todo needs a description.");
        }

        // Create and add the todo task
        Task task = new Todo(description);
        tasks.add(task);
        saveTasks(tasks); // Save to file after adding todo task
        ui.printBox(
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
    private void handleDeadline(ArrayList<Task> tasks, String userInput) throws BuddyException {
        String content = userInput.length() > 9 ? userInput.substring(9).trim() : "";
        int byIndex = content.indexOf(" /by ");
        
        // Check for valid format
        if (content.isEmpty() || byIndex == -1) {
            throw new BuddyException("Deadline format: deadline <desc> /by <time>");
        }

        String description = content.substring(0, byIndex).trim();
        String by = content.substring(byIndex + 5).trim();

        try {
            // Create and add the deadline task
            Task task = new Deadline(description, by);
            tasks.add(task);
            saveTasks(tasks);
            ui.printBox(
                "Got it. I've added this task:",
                "  " + task,
                "Now you have " + tasks.size() + " tasks in the list."
            );
        } catch (IllegalArgumentException e) {
            throw new BuddyException(e.getMessage());
        }
    }

    /**
     * Handler for the 'event' command.
     *
     * @param tasks the list of tasks
     * @param userInput the full user input string
     * @throws BuddyException if the description, start time, or end time is invalid
     */
    private void handleEvent(ArrayList<Task> tasks, String userInput) throws BuddyException {
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

        try {
            // Create and add the event task
            Task task = new Event(description, from, to);
            tasks.add(task);
            saveTasks(tasks);
            ui.printBox(
                "Got it. I've added this task:",
                "  " + task,
                "Now you have " + tasks.size() + " tasks in the list."
            );
        } catch (IllegalArgumentException e) {
            throw new BuddyException(e.getMessage());
        }
    }

    public void run() {
        // List to store tasks
        ArrayList<Task> tasks = loadTasks();

        // Print welcome message
        ui.showWelcome();
        
        // Read the first user input
        String userInput = ui.readCommand();

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
                case FIND -> handleFind(tasks, userInput);
                default -> throw new BuddyException("I don't recognize that command.");
                }
            } catch (BuddyException e) {
                ui.showError(e.getMessage());
            }

            // Read the next user input
            userInput = ui.readCommand();
        }

        // Close the scanner and print goodbye message
        ui.close();
        ui.showGoodbye();
    }
    
    /**
     * Main method to run the Buddy application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Buddy().run();
    }
}
