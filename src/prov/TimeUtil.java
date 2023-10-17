package prov;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class TimeUtil {

	private TimeUtil() {
	}

	public static boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
		return !time.isBefore(start) && !time.isAfter(end);
	}
	
	public static boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
	    return !date.isBefore(start) && !date.isAfter(end);
	}
	
	public static boolean isDateRangeValid(LocalDate start, LocalDate end) {
		return !start.isAfter(end);
	}

	public static int calculateDaysBetween(LocalDate startLocalDate, LocalTime startLocalTime, LocalDate endLocalDate, LocalTime endLocalTime) {
		LocalDateTime startDateTime = LocalDateTime.of(startLocalDate, startLocalTime);
		LocalDateTime endDateTime = LocalDateTime.of(endLocalDate, endLocalTime);

		long days = ChronoUnit.DAYS.between(startDateTime, endDateTime);
		if (endDateTime.isAfter(startDateTime.plusDays(days))) {
			days++;
		}
		return (int) days;
	}
	
    public static String getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
    }
    
    public static boolean isInnerRangeWithinOuterRange(LocalTime outerStart, LocalTime outerEnd, LocalTime innerStart, LocalTime innerEnd) {
        return !innerStart.isBefore(outerStart) && !innerEnd.isAfter(outerEnd);
    }
}
