package bong.command;

import bong.exception.BongException;
import bong.storage.Storage;
import bong.task.TaskList;
import bong.ui.Ui;

/**
 * Represents an abstract command, where all specific commands extend this class.
 * Provides an execute method to perform command logic and an isExit method to
 * signal if the command should terminate the application.
 */
public abstract class Command {

    /**
     * Executes the command logic.
     * This method is implemented by concrete command classes to perform
     * actions on the task list, interact with the UI, and update storage.
     *
     * @param tasks The TaskList object to operate on.
     * @param ui The Ui object for displaying messages.
     * @param storage The Storage object for saving/loading tasks.
     * @return String message representing the outcome of the command execution.
     * @throws BongException If an error occurs during command execution.
     */
    public abstract String execute(TaskList tasks, Ui ui, Storage storage) throws BongException;

    /**
     * Checks if this command is an exit command.
     *
     * @return True if this command signifies application exit, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
