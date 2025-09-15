package bong.command;

import bong.exception.BongException;
import bong.storage.Storage;
import bong.task.Task;
import bong.task.TaskList;
import bong.ui.Ui;

import java.io.IOException;

/**
 * Represents a command to delete a task from the list.
 * Inherits from Command.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Constructs a DeleteCommand with the specified task number.
     *
     * @param taskNumber The 1-based index of the task.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Executes the delete command.
     * Deletes the specified task from the TaskList, saves the updated list to storage,
     * and returns a confirmation message.
     *
     * @param tasks The TaskList object to delete the task from.
     * @param ui The Ui object (not directly used).
     * @param storage The Storage object to save the updated tasks.
     * @return String message confirming the task deletion and current task count.
     * @throws BongException If the task number is out of bounds or an error occurs during saving.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws BongException {
        Task removedTask = tasks.deleteTask(taskNumber);
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException e) {
            throw new BongException("Error saving tasks: " + e. getMessage());
        }
        return "    Noted. I've removed this task:\n" + "    " +
                removedTask.toString() + "\n    Now you have " +
                tasks.size() + " tasks in the list.";
    }
}
