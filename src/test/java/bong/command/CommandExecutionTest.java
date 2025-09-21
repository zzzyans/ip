package bong.command;

import bong.storage.Storage;
import bong.task.Deadline;
import bong.task.Event;
import bong.task.Task;
import bong.task.TaskList;
import bong.task.Todo;
import bong.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import bong.util.DateTimeUtil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for basic command execution behaviours.
 */
public class CommandExecutionTest {

    @Test
    void todoCommand_addsTask_and_persists(@TempDir Path tmp) throws Exception {
        Storage storage = new Storage(tmp.resolve("bong.txt").toString());
        Ui ui = new Ui();
        TaskList tasks = new TaskList();

        TodoCommand todoCmd = new TodoCommand("read book");
        String res = todoCmd.execute(tasks, ui, storage);

        assertTrue(res.contains("Got it"));
        assertEquals(1, tasks.size());
        assertEquals("read book", tasks.getTasks().get(0).getDescription());

        // Ensure persistence (load from file)
        List<Task> loaded = storage.loadTasks(ui);
        assertEquals(1, loaded.size());
        assertEquals("read book", loaded.get(0).getDescription());

        ui.closeScanner();
    }

    @Test
    void mark_unmark_and_delete_commands_work_and_persist(@TempDir Path tmp) throws Exception {
        Storage storage = new Storage(tmp.resolve("bong.txt").toString());
        Ui ui = new Ui();
        TaskList tasks = new TaskList();

        // Add two todos via direct TaskList (simulate prior state)
        tasks.addTask(new Todo("A"));
        tasks.addTask(new Todo("B"));
        // Save initial state
        storage.saveTasks(tasks.getTasks());

        // Mark task 2
        MarkCommand markCmd = new MarkCommand(2);
        String markRes = markCmd.execute(tasks, ui, storage);
        assertTrue(markRes.contains("marked"));
        assertTrue(tasks.getTask(2).isDone());

        // Unmark task 2
        UnmarkCommand unmarkCmd = new UnmarkCommand(2);
        String unmarkRes = unmarkCmd.execute(tasks, ui, storage);
        assertTrue(unmarkRes.contains("not done"));
        assertFalse(tasks.getTask(2).isDone());

        // Delete task 1
        DeleteCommand deleteCmd = new DeleteCommand(1);
        String delRes = deleteCmd.execute(tasks, ui, storage);
        assertTrue(delRes.contains("removed"));
        assertEquals(1, tasks.size());

        // Reload from storage to confirm persisted changes
        List<Task> reloaded = storage.loadTasks(ui);
        assertEquals(1, reloaded.size()); // only one left
        ui.closeScanner();
    }

    @Test
    void find_and_snooze_commands_work_with_deadlines_and_events(@TempDir Path tmp) throws Exception {
        Storage storage = new Storage(tmp.resolve("bong.txt").toString());
        Ui ui = new Ui();
        TaskList tasks = new TaskList();

        // Create a deadline and event
        Deadline d = new Deadline("return book", "2025-10-16 1200");
        Event e = new Event("meeting", "2025-09-30 1200", "2025-09-30 1500");
        tasks.addTask(d);
        tasks.addTask(e);
        storage.saveTasks(tasks.getTasks());

        // Find 'book'
        FindCommand findCmd = new FindCommand("book");
        String findRes = findCmd.execute(tasks, ui, storage);
        assertTrue(findRes.toLowerCase().contains("return book"));

        // Snooze deadline (task 1)
        SnoozeCommand snoozeDeadline = new SnoozeCommand(1, "2025-10-18 1300", null);
        String snoozeRes = snoozeDeadline.execute(tasks, ui, storage);
        assertTrue(snoozeRes.toLowerCase().contains("snoozed"));
        // Confirm in-memory updated
        Deadline updated = (Deadline) tasks.getTask(1);
        assertEquals("2025-10-18 1300", updated.getDeadline().format(DateTimeUtil.INPUT));

        // Snooze event (task 2) with new start and end
        SnoozeCommand snoozeEvent = new SnoozeCommand(2, "2025-10-20 0900", "2025-10-20 1200");
        snoozeEvent.execute(tasks, ui, storage);
        Event updatedEvent = (Event) tasks.getTask(2);
        assertEquals("2025-10-20 0900", updatedEvent.getStart().format(DateTimeUtil.INPUT));
        assertEquals("2025-10-20 1200", updatedEvent.getEnd().format(DateTimeUtil.INPUT));

        // Confirm persistence by reloading
        List<Task> reloaded = storage.loadTasks(ui);
        assertEquals(2, reloaded.size());
        assertTrue(reloaded.get(0).toString().toLowerCase().contains("oct 18"));
        assertTrue(reloaded.get(1).toString().toLowerCase().contains("oct 20"));

        ui.closeScanner();
    }
}