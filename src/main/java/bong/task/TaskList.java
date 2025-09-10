package bong.task;

import java.util.ArrayList;
import java.util.List;

import bong.exception.BongException;

/**
 * Manages a list of tasks for the Bong application.
 * It provides methods to add, delete, mark and unmark tasks,
 * as well as retrieve tasks and the current size of the list.
 */
public class TaskList {
    private List<Task> tasks;

    /**
     * Constructs an empty TaskList.
     * Initialises the internal list to an empty ArrayList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with an initial list of tasks.
     * This is used when loading tasks from storage.
     * 
     * @param tasks The initial List<Task> of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the task list.
     *
     * @param task The Task object to be added.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Deletes a task from the list based on its 1-based index.
     *
     * @param taskIndex The 1-based index of the task to delete.
     * @return The Task object that was removed from the list.
     * @throws BongException If the provided taskIndex is out of the valid range.
     */
    public Task deleteTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        return tasks.remove(taskIndex - 1);
    }

    /**
     * Marks a task as done based on its 1-based index.
     *
     * @param taskIndex The 1-based index of the task to mark as done.
     * @return The Task object that was marked.
     * @throws BongException If the provided taskIndex is out of the valid range.
     */
    public Task markTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        tasks.get(taskIndex - 1).mark();
        return tasks.get(taskIndex - 1);
    }

    /**
     * Marks a task as not done based on its 1-based index.
     *
     * @param taskIndex The 1-based index of the task to unmark.
     * @return The Task object that was unmarked.
     * @throws BongException If the provided taskIndex is out of the valid range.
     */
    public Task unmarkTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        tasks.get(taskIndex - 1).unmark();
        return tasks.get(taskIndex - 1);
    }

    /**
     * Retrieves a task from the list based on its 1-based index.
     *
     * @param taskIndex The 1-based index of the task to retrieve.
     * @return The Task object at the specified index.
     * @throws BongException If the provided taskIndex is out of the valid range.
     */
    public Task getTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        return tasks.get(taskIndex - 1);
    }

    /**
     * Returns the current number of tasks in the list.
     *
     * @return An integer representing the total count of tasks.
     */
    public int size() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
