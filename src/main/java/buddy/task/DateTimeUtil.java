package buddy.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Common date/time parsing and formatting utilities.
 */
public final class DateTimeUtil {
    public static final DateTimeFormatter FILE_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    public static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
    public static final DateTimeFormatter FIND_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeUtil() {
    }


    /**
     * Parses a date/time string from the file format.
     * @param dateTimeStr the date/time string in file format
     * @return the parsed LocalDateTime object
     */
    public static LocalDateTime parseFileDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, FILE_DATE_TIME_FORMAT);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid date/time format. Use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
        }
    }

    /**
     * Formats a LocalDateTime object to the file date/time string format.
     * @param dateTime the LocalDateTime object to format
     * @return the formatted date/time string
     */
    public static String formatFileDateTime(LocalDateTime dateTime) {
        return dateTime.format(FILE_DATE_TIME_FORMAT);
    }

    /**
     * Formats a LocalDateTime object to a user-friendly display string.
     * @param dateTime the LocalDateTime object to format
     * @return the formatted display date/time string
     */
    public static String formatDisplayDateTime(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_DATE_TIME_FORMAT);
    }

    /**
     * Parses a date string for the find command.
     * @param dateStr the date string in find command format
     * @return the parsed LocalDate object
     */
    public static LocalDate parseFindDate(String dateStr) {
        return LocalDate.parse(dateStr, FIND_DATE_FORMAT);
    }
}
