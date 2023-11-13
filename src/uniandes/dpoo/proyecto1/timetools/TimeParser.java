package uniandes.dpoo.proyecto1.timetools;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeParser {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    private static final DateTimeFormatter DATE_FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private TimeParser() {}

    public static Object parse(String timeStr) {
        if (isLocalDate(timeStr)) {
            try {
                return LocalDate.parse(timeStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                return LocalDate.parse(timeStr, DATE_FORMATTER2);
            }
        } else if (isLocalTime(timeStr)) {
            return LocalTime.parse(timeStr);
        } else {
            throw new IllegalArgumentException("Invalid time format. Expected 'YYYY_MM_DD', 'YYYY-MM-DD' or 'HH:MM'");
        }
    }

    private static boolean isLocalDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e1) {
            try {
                LocalDate.parse(dateStr, DATE_FORMATTER2);
                return true;
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
    }

    private static boolean isLocalTime(String timeStr) {
        try {
            LocalTime.parse(timeStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
