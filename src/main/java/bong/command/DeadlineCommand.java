package bong.command;

import bong.exception.BongException;
import bong.task.Deadline;

/**
 * Represents a command to add a Deadline task.
 * Inherits from AddCommand.
 */
public class DeadlineCommand extends AddCommand {

    private final String deadlineTimeStr;

    /**
     * Constructs a DeadlineCommand with the given description and deadline.
     *
     * @param description Description of the Deadline task.
     * @param deadline Deadline date and time as a string.
     * @throws BongException If the deadline string is in an invalid format.
     */
    public DeadlineCommand(String description, String deadline) throws BongException {
        super(new Deadline(description, deadline));
        this.deadlineTimeStr = deadline;
    }

    public String getDescription() {
        return this.newTask.getDescription();
    }

    public String getDeadlineTime() {
        return this.deadlineTimeStr;
    }
}
