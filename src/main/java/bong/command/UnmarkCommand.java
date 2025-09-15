package bong.command;

import bong.exception.BongException;
import bong.storage.Storage;
import bong.task.Task;
import bong.task.TaskList;
import bong.ui.Ui;

import java.io.IOException;

/**
 * Represents a command to mark a task as not done.
 * Inherits from Command.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Constructs a UnmarkCommand with the specified task number.
     *
     * @param taskNumber The 1-based index of the task.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Executes the unmark command.
     * Marks the specified task as not done, saves the updated list to storage,
     * and returns a confirmation message.
     *
     * @param tasks The TaskList object to unmark the task in.
     * @param ui The Ui object (not directly used).
     * @param storage The Storage object to save the updated tasks.
     * @return String message confirming the task has been unmarked.
     * @throws BongException If the task number is out of bounds or an error occurs during saving.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws BongException {
        Task unmarkedTask = tasks.unmarkTask(taskNumber);
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException e) {
            throw new BongException("Error saving tasks: " + e. getMessage());
        }
        return "Nice! I've marked this task as not done yet:\n" + unmarkedTask.toString();
    }
}
