package bong.command;

import bong.exception.BongException;
import bong.storage.Storage;
import bong.task.Task;
import bong.task.TaskList;
import bong.ui.Ui;

import java.io.IOException;

/**
 * Abstract base class for commands that add a task to the list.
 * Handles the common logic for adding a task and saving the updated list.
 */
public abstract class AddCommand extends Command {
    protected Task newTask;

    /**
     * Constructs an AddCommand with the specified task.
     *
     * @param task The task to be added.
     */
    public AddCommand(Task task) {
        this.newTask = task;
    }

    /**
     * Executes the add command.
     * Adds the task to the TaskList, saves the updated list to storage, and
     * returns a confirmation message.
     *
     * @param tasks The TaskList object to add the task to.
     * @param ui The Ui object (not directly used).
     * @param storage The Storage object to save the updated tasks.
     * @return String message confirming the task addition and current task count.
     * @throws BongException If an error occurs during saving tasks.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws BongException {
        tasks.addTask(newTask);
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException e) {
            throw new BongException("Error saving tasks: " + e.getMessage());
        }
        return "Got it. I've added this task:\n" +
                newTask.toString() + "\n" +
                "Now you have " + tasks.size() + " tasks in the list.";
    }
}
