package bong.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bong.exception.BongException;

/**
 * Represents a task that needs to be completed by a specific date and time.
 * It extends the base Task class and includes a deadline (LocalDateTime).
 */
public class Deadline extends Task {
    protected LocalDateTime deadline;

    // Input date/time format for parsing user input
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    // Output date/time format for saving to file
    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT =  
        DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");

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
            this.deadline = LocalDateTime.parse(deadline, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BongException("Invalid deadline date/time format!\n" +
                    "Please use 'yyyy-MM-dd HHmm' (eg. 2019-10-15 1800).");
        }
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadline.format(OUTPUT_DATE_TIME_FORMAT) + ")";
    }

}