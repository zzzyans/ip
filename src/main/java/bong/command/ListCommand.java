package bong.command;

import bong.storage.Storage;
import bong.task.Task;
import bong.task.TaskList;
import bong.ui.Ui;

import java.util.List;

/**
 * Represents a command to list all tasks in the TaskList.
 * Inherits from the abstract Command class.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command.
     * It retrieves all tasks from the TaskList and returns a formatted string
     * containing the list of tasks.
     *
     * @param tasks The TaskList object containing the tasks.
     * @param ui The Ui object (not directly used).
     * @param storage The Storage object (not directly used).
     * @return String message containing the formatted list of tasks.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> currentTasks = tasks.getTasks();
        StringBuilder response = new StringBuilder();

        if (currentTasks.isEmpty()) {
            response.append("    Your task list is empty. Time to add some tasks!");
        } else {
            response.append("    Here are the tasks in your list:\n");
            for (int i = 0; i < currentTasks.size(); i++) {
                response.append("    ").append(i + 1).append(". ").append(currentTasks.get(i).toString()).append("\n");
            }
        }
        return response.toString();
    }
}
