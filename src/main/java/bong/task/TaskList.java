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
        tasks.get(taskIndex - 1).setMark();
        return tasks.get(taskIndex - 1);
    }

    public Task unmarkTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        tasks.get(taskIndex - 1).setUnmark();
        return tasks.get(taskIndex - 1);
    }

    public Task getTask(int taskIndex) throws BongException {
        if (taskIndex <= 0 || taskIndex > tasks.size()) {
            throw new BongException("You do not have this many tasks in your list!");
        }
        return tasks.get(taskIndex - 1);
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
