package bong.command;

import bong.task.Todo;

/**
 * Represents a command to add a Todo task.
 * Inherits from AddCommand.
 */
public class TodoCommand extends AddCommand {

    /**
     * Constructs a TodoCommand with the given description.
     *
     * @param description Description of the Todo task.
     */
    public TodoCommand(String description) {
        super(new Todo(description));
    }
}
