package buddy.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import buddy.BuddyException;
import buddy.Storage;
import buddy.Ui;
import buddy.task.TaskList;
import buddy.task.Todo;

public class MarkCommandTest {

    @Test
    void execute_validIndex_marksTaskAndSaves() throws BuddyException {
        TaskList taskList = new TaskList();
        taskList.addTask(new Todo("read book"));

        FakeStorage storage = new FakeStorage();
        FakeUi ui = new FakeUi();

        MarkCommand command = new MarkCommand("mark 1");
        command.execute(taskList, ui, storage);

        assertEquals("X", taskList.getTask(0).getStatusIcon());
        assertTrue(storage.saveCalled);
        assertEquals(List.of("T | 1 | read book"), storage.lastSaved);
    }

    @Test
    void execute_outOfRange_throwsBuddyException() {
        TaskList taskList = new TaskList();
        FakeStorage storage = new FakeStorage();
        FakeUi ui = new FakeUi();

        MarkCommand command = new MarkCommand("mark 1");

        BuddyException ex = assertThrows(BuddyException.class, () -> command.execute(taskList, ui, storage));
        assertEquals("Task number does not exist.", ex.getMessage());
    }

    @Test
    void execute_invalidNumber_throwsBuddyException() {
        TaskList taskList = new TaskList();
        FakeStorage storage = new FakeStorage();
        FakeUi ui = new FakeUi();

        MarkCommand command = new MarkCommand("mark abc");

        BuddyException ex = assertThrows(BuddyException.class, () -> command.execute(taskList, ui, storage));
        assertEquals("Please provide a valid task number.", ex.getMessage());
    }

    private static class FakeStorage extends Storage {
        boolean saveCalled = false;
        List<String> lastSaved = List.of();

        FakeStorage() {
            super("dummy.txt");
        }

        @Override
        public void save(List<String> tasks) {
            saveCalled = true;
            lastSaved = tasks;
        }
    }

    private static class FakeUi extends Ui {
        @Override
        public void printBox(String... lines) {
            // no-op for tests
        }

        @Override
        public void showError(String message) {
            // no-op for tests
        }
    }
}