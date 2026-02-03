package buddy.task;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskList manages a list of tasks with operations to add, delete, and retrieve tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task at the specified index.
     *
     * @param index the 0-based index of the task to remove
     * @return the removed task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Gets a task at the specified index.
     *
     * @param index the 0-based index of the task
     * @return the task at the specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns all tasks as a list.
     *
     * @return a copy of the task list
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Converts all tasks to file format strings.
     *
     * @return a list of task strings in file format
     */
    public List<String> toFileFormat() {
        ArrayList<String> taskStrings = new ArrayList<>();
        for (Task task : tasks) {
            taskStrings.add(task.toFileFormat());
        }
        return taskStrings;
    }
}
