package bong.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import bong.exception.BongException;
import bong.util.DateTimeUtil;

/**
 * Represents a task that needs to be completed by a specific date and time.
 * It extends the base Task class and includes a deadline (LocalDateTime).
 */
public class Deadline extends Task {
    protected LocalDateTime deadline;

    /**
     * Constructs a Deadline task with the given description and deadline string.
     * The deadline string is parsed into a LocalDateTime object.
     *
     * @param description The description of the deadline task.
     * @param deadline The deadline date and time as a string in "yyyy-MM-dd HHmm" format.
     * @throws BongException If the provided deadline string is in an invalid format.
     */
    public Deadline(String description, String deadline) throws BongException {
        super(description);
        try {
            this.deadline = LocalDateTime.parse(deadline, DateTimeUtil.INPUT);
        } catch (DateTimeParseException e) {
            throw new BongException("Invalid deadline date/time format!\n" +
                    "Please use 'yyyy-MM-dd HHmm' (eg. 2019-10-15 1800).");
        }
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    /**
     * Sets a new deadline for this task.
     *
     * @param newDeadline The new deadline.
     */
    public void setDeadline(LocalDateTime newDeadline) {
        assert newDeadline != null : "new deadline must not be null";
        this.deadline = newDeadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadline.format(DateTimeUtil.OUTPUT) + ")";
    }

}