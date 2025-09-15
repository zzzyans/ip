package bong.command;

import bong.storage.Storage;
import bong.task.Task;
import bong.task.TaskList;
import bong.ui.Ui;

import java.util.List;

/**
 * Represents a command to find tasks by a keyword in their description.
 * Inherits from Command.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the specified keyword.
     *
     * @param keyword Keyword to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command.
     * Searches the TaskList for tasks containing the keyword and returns a formatted string
     * listing the matching tasks or a "no matches" message.
     *
     * @param tasks The TaskList object to search within.
     * @param ui The Ui object (not directly used).
     * @param storage The Storage object (not directly used).
     * @return String message with the list of matching tasks or a no-match message.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.findTasks(keyword);
        StringBuilder response = new StringBuilder();

        if (matchingTasks.isEmpty()) {
            response.append("    No matching tasks found in your list.");
        } else {
            response.append("    Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                response.append("    ").append(i + 1).append(". ").append(matchingTasks.get(i).toString()).append("\n");
            }
        }
        return response.toString();
    }
}
