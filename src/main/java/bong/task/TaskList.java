package bong.task;

import java.util.ArrayList;
import java.util.List;

import bong.exception.BongException;

public class TaskList {
    private List<Task> tasks;

    /*
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /*
     * Constructs a TaskList with an initial list of tasks.
     * This is useful when loading tasks from storage.
     * 
     * @param tasks The initial list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public Task deleteTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        return tasks.remove(taskIndex - 1);
    }

    public Task markTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        tasks.get(taskIndex - 1).mark();
        return tasks.get(taskIndex - 1);
    }

    public Task unmarkTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        tasks.get(taskIndex - 1).unmark();
        return tasks.get(taskIndex - 1);
    }

    public Task getTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        return tasks.get(taskIndex - 1);
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
