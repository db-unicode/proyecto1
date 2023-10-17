package prov;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// T only can be LocalDate or LocalTime 
public class TimeRange<T> {
    private T start;
    private T end;

    @SuppressWarnings("unchecked")
    public TimeRange(String startStr, String endStr) {
        this.start = (T) TimeParser.parse(startStr);
        this.end = (T) TimeParser.parse(endStr);

        if (!start.getClass().equals(end.getClass())) {
            throw new IllegalArgumentException("Both times must be of the same type (either LocalDate or LocalTime)");
        }

        // Check if start is before end
        if (start instanceof LocalDate && ((LocalDate) start).isAfter((LocalDate) end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        } else if (start instanceof LocalTime && ((LocalTime) start).isAfter((LocalTime) end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    public String getStartAsString() {
        return formatValue(start);
    }

    public String getEndAsString() {
        return formatValue(end);
    }

    private String formatValue(T value) {
        if (value instanceof LocalDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
            return ((LocalDate) value).format(formatter);
        } else if (value instanceof LocalTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return ((LocalTime) value).format(formatter);
        } else {
            throw new IllegalArgumentException("Unsupported type");
        }
    }
}


