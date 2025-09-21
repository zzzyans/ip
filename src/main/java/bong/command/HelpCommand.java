package bong.command;

import bong.storage.Storage;
import bong.task.TaskList;
import bong.ui.Ui;

/**
 * Represents a command that returns text describing available commands and their formats.
 * Used GPT-5 mini (high) to generate this code based off my other files in bong.command.
 * Inherits from Command.
 */
public class HelpCommand extends Command {

    private static final String HELP_TEXT =
            "Available commands:\n" +
                    "1. Add a todo task.\n" +
                    "     todo <description>\n" +
                    "2. Add a deadline task.\n" +
                    "     deadline <description> /by <yyyy-MM-dd HHmm>\n" +
                    "3. Add an event task.\n" +
                    "     event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n" +
                    "4. List all tasks.\n" +
                    "     list\n" +
                    "5. Mark task as done.\n" +
                    "     mark <task number>\n" +
                    "6. Mark task as not done.\n" +
                    "     unmark <task number>\n" +
                    "7. Delete a task.\n" +
                    "     delete <task number>\n" +
                    "8. Find tasks whose description contains the keyword.\n" +
                    "     find <keyword>\n" +
                    "9. Reschedule a deadline or an event task.\n" +
                    "     snooze <task number> /to <yyyy-MM-dd HHmm> (/end <yyyy-MM-dd HHmm>)\n" +
                    "10. Show this help message.\n" +
                    "       help\n" +
                    "11. Exit the application.\n" +
                    "       bye\n";

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return HELP_TEXT;
    }
}