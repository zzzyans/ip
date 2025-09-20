package bong.util;

import java.time.format.DateTimeFormatter;

/**
 * Centralised date/time formatters used across the project.
 */
public final class DateTimeUtil {
    private DateTimeUtil() {}

    public static final DateTimeFormatter INPUT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    public static final DateTimeFormatter STORAGE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    public static final DateTimeFormatter OUTPUT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm");
}
