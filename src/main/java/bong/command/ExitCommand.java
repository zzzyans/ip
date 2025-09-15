package bong.command;

import bong.storage.Storage;
import bong.task.Task;
import bong.task.TaskList;
import bong.ui.Ui;

import java.util.List;

/**
 * Represents a command to exit the Bong application.
 * Inherits from the abstract Command class.
 */
public class ExitCommand extends Command {
    /**
     * Executes the exit command.
     *
     * @param tasks The TaskList object (not directly used).
     * @param ui The Ui object (not directly used).
     * @param storage The Storage object (not directly used).
     * @return String message confirming the exit.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return "Bye, hope to see you again soon!";
    }

    /**
     * Indicates that this command is an exit command.
     *
     * @return True.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
