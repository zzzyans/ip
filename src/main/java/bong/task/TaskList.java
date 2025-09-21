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
        assert task != null : "task passed to addTask must not be null";
        this.tasks.add(task);
    }

    public Task deleteTask(int taskIndex) throws BongException {
        assert tasks != null : "tasks list must be non-null";
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        Task removed = tasks.remove(taskIndex - 1);
        assert removed != null : "removed task should not be null after remove";
        return removed;
    }

    public Task markTask(int taskIndex) throws BongException {
        assert tasks != null : "tasks list must be non-null";
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        tasks.get(taskIndex - 1).setMark();
        Task t = tasks.get(taskIndex - 1);
        assert t != null : "task at index must exist after mark";
        return t;
    }

    public Task unmarkTask(int taskIndex) throws BongException {
        assert tasks != null : "tasks list must be non-null";
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        tasks.get(taskIndex - 1).setUnmark();
        Task t = tasks.get(taskIndex - 1);
        assert t != null : "task at index must exist after unmark";
        return t;
    }

    public Task getTask(int taskIndex) throws BongException {
        assert tasks != null : "tasks list must be non-null";
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        Task t = tasks.get(taskIndex - 1);
        assert t != null : "task at index must not be null";
        return t;
    }


    /**
     * Finds tasks in the list whose descriptions contain the specified keyword (case-insensitive).
     *
     * @param keyword The string to search for within task descriptions.
     * @return A List<Task> containing all matching tasks.
     */
    public List<Task> findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        String lowercaseKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowercaseKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
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
