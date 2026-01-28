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
                    Task task = TaskParser.parseFromFile(line);
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
            int taskIndex = Parser.parseTaskNumber(userInput, 4);
            
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
            int taskIndex = Parser.parseTaskNumber(userInput, 7);
            
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
            int taskIndex = Parser.parseTaskNumber(userInput, 7);
            
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
        String dateString = Parser.parseFindDate(userInput);

        try {
            java.time.LocalDate searchDate = java.time.LocalDate.parse(dateString, 
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
        String description = Parser.parseTodoDescription(userInput);

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
        String[] parts = Parser.parseDeadline(userInput);
        String description = parts[0];
        String by = parts[1];

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
        String[] parts = Parser.parseEvent(userInput);
        String description = parts[0];
        String from = parts[1];
        String to = parts[2];

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
            Parser.CommandType cmd = Parser.parseCommand(userInput);

            // Exit if command is BYE
            if (cmd == Parser.CommandType.BYE) {
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
