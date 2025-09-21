package bong.command;

import bong.exception.BongException;
import bong.task.Event;

/**
 * Represents a command to add an Event task.
 * Inherits from AddCommand
 */
public class EventCommand extends AddCommand {

    private final String startStr;
    private final String endStr;

    /**
     * Constructs an EventCommand with the given description, start time and end time.
     *
     * @param description The description of the Event task.
     * @param start The event start date and time as a string.
     * @param end The event end date and time as a string.
     * @throws BongException If the start or end time string is in an invalid format.
     */
    public EventCommand(String description, String start, String end) throws BongException {
        super(new Event(description, start, end));
        this.startStr = start;
        this.endStr = end;
    }

    public String getDescription() {
        return this.newTask.getDescription();
    }

    public String getStartTime() {
        return this.startStr;
    }

    public String getEndTime() {
        return this.endStr;
    }
}
