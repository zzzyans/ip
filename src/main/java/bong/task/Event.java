package bong.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import bong.exception.BongException;
import bong.util.DateTimeUtil;

/**
 * Represents a task that is an event, occurring within a specific start and end time.
 * It extends the base Task class and includes a start and end time (LocalDateTime).
 */
public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Constructs an Event task with the given description, start time string, and end time string.
     * The start and end time strings are parsed into LocalDateTime objects.
     *
     * @param description The description of the event task.
     * @param start The event start date and time as a string in "yyyy-MM-dd HHmm" format.
     * @param end The event end date and time as a string in "yyyy-MM-dd HHmm" format.
     * @throws BongException If either the start or end date/time string is in an invalid format.
     */
    public Event(String description, String start, String end) throws BongException {
        super(description);
        try {
            this.start = LocalDateTime.parse(start, DateTimeUtil.INPUT);
        } catch (DateTimeParseException e) {
            throw new BongException("Invalid event start date/time format!\n" +
                    "Please use 'yyyy-MM-dd HHmm' (eg. 2019-10-15 1800).");
        }
        try {
            this.end = LocalDateTime.parse(end, DateTimeUtil.INPUT);
        } catch (DateTimeParseException e) {
            throw new BongException("Invalid event end date/time format!\n" +
                    "Please use 'yyyy-MM-dd HHmm' (eg. 2019-10-15 1800).");
        }
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    /**
     * Sets a new start time for this event.
     *
     * @param newStart The new start time.
     */
    public void setStart(LocalDateTime newStart) {
        assert newStart != null : "new start time must not be null";
        this.start = newStart;
    }

    /**
     * Sets a new end time for the event.
     *
     * @param newEnd The new end time.
     */
    public void setEnd(LocalDateTime newEnd) {
        assert newEnd != null : "new end time must not be null";
        this.end = newEnd;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from " + this.start.format(DateTimeUtil.OUTPUT)
                + " to " + this.end.format(DateTimeUtil.OUTPUT) + ")";
    }
}
