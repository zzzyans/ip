package bong.task;

import bong.exception.BongException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    void addTask_singleTask_sizeIncreases() {
        Task task = new Task("Test Task");
        taskList.addTask(task);

        assertEquals(1, taskList.size());
        assertEquals(task, taskList.getTasks().get(0));
    }

    @Test
    void addTask_multipleTasks_correctOrderAndSize() throws BongException {
        Task task1 = new Todo("Read Book");
        Task task2 = new Deadline("Return Book", "2025-10-16 1800");
        taskList.addTask(task1);
        taskList.addTask(task2);

        assertEquals(2, taskList.size());
        assertEquals(task1, taskList.getTasks().get(0));
        assertEquals(task2, taskList.getTasks().get(1));
    }

    @Test
    void deleteTask_validIndex_taskRemovedAndSizeDecreases() throws BongException {
        Task task1 = new Todo("Task One");
        Task task2 = new Todo("Task Two");
        taskList.addTask(task1);
        taskList.addTask(task2);

        Task removed = taskList.deleteTask(1);

        assertEquals(task1, removed);
        assertEquals(1, taskList.size());
        assertEquals(task2, taskList.getTasks().get(0));
    }

    @Test
    void deleteTask_lastTask_listBecomesEmpty() throws BongException {
        Task task = new Todo("Single Task");
        taskList.addTask(task);

        Task removed = taskList.deleteTask(1);

        assertEquals(task, removed);
        assertEquals(0, taskList.size());
        assertTrue(taskList.getTasks().isEmpty());
    }

    @Test
    void deleteTask_invalidIndexTooSmall_throwsBongException() {
        taskList.addTask(new Todo("Task One"));
        Exception exception = assertThrows(BongException.class, () -> taskList.deleteTask(0));
        assertEquals("You do not have this many tasks in your list!", exception.getMessage());
        assertEquals(1, taskList.size());
    }

    @Test
    void deleteTask_invalidIndexTooLarge_throwsBongException() {
        taskList.addTask(new Todo("Task One"));
        Exception exception = assertThrows(BongException.class, () -> taskList.deleteTask(2));
        assertEquals("You do not have this many tasks in your list!", exception.getMessage());
        assertEquals(1, taskList.size());
    }

    @Test
    void markTask_validIndex_taskIsDone() throws BongException {
        Task task = new Todo("Unmarked Task");
        taskList.addTask(task);
        assertFalse(task.isDone());

        Task marked = taskList.markTask(1);

        assertTrue(marked.isDone());
        assertTrue(task.isDone());
    }

    @Test
    void markTask_invalidIndex_throwsBongException() {
        taskList.addTask(new Todo("Task One"));
        Exception exception = assertThrows(BongException.class, () -> taskList.markTask(0));
        assertEquals("You do not have this many tasks in your list!", exception.getMessage());
    }

    @Test
    void unmarkTask_validIndex_taskIsDone() throws BongException {
        Task task = new Todo("Marked Task");
        task.mark();
        taskList.addTask(task);
        assertTrue(task.isDone());

        Task unmarked = taskList.unmarkTask(1);

        assertFalse(unmarked.isDone());
        assertFalse(task.isDone());
    }

    @Test
    void unmarkTask_invalidIndex_throwsBongException() {
        taskList.addTask(new Todo("Task One"));
        Exception exception = assertThrows(BongException.class, () -> taskList.markTask(99));
        assertEquals("You do not have this many tasks in your list!", exception.getMessage());
    }

    @Test
    void getTask_validIndex_returnsCorrectTask() throws BongException {
        Task task1 = new Todo("Task One");
        Task task2 = new Todo("Task Two");
        taskList.addTask(task1);
        taskList.addTask(task2);

        assertEquals(task1, taskList.getTask(1));
        assertEquals(task2, taskList.getTask(2));
    }

    @Test
    void getTask_invalidIndex_throwsBongException() {
        taskList.addTask(new Todo("Task One"));
        assertThrows(BongException.class, () -> taskList.getTask(0));
        assertThrows(BongException.class, () -> taskList.getTask(2));
    }

    @Test
    void size_emptyList_returnsZero() {
        assertEquals(0, taskList.size());
    }

    @Test
    void size_afterAddingTasks_returnsCorrectCount() {
        taskList.addTask(new Todo("Task One"));
        taskList.addTask(new Todo("Task Two"));
        assertEquals(2, taskList.size());
    }

    @Test
    void size_afterDeletingTasks_returnsCorrectCount() throws BongException {
        taskList.addTask(new Todo("Task One"));
        taskList.addTask(new Todo("Task Two"));
        taskList.deleteTask(1);
        assertEquals(1, taskList.size());
    }
}

