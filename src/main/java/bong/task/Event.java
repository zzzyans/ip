package bong.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bong.exception.BongException;

public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;

    // Input date/time format for parsing user input
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    // Output date/time format for saving to file
    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT =  
        DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");

    public Event(String description, String start, String end) throws BongException {
        super(description);
        try {
            this.start = LocalDateTime.parse(start, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BongException("   Invalid event start date/time format!\n" +
                    "    Please use 'yyyy-MM-dd HHmm' (eg. 2019-10-15 1800).");
        }
        try {
            this.end = LocalDateTime.parse(end, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BongException("   Invalid event end date/time format!\n" +
                    "    Please use 'yyyy-MM-dd HHmm' (eg. 2019-10-15 1800).");
        }
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from " + this.start.format(OUTPUT_DATE_TIME_FORMAT)
                + " to " + this.end.format(OUTPUT_DATE_TIME_FORMAT) + ")";
    }
}
