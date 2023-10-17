package prov;


import java.time.LocalTime;


import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;


public class LocationQueries {
	private static final Table locationTable = TablesManager.getTable("location");
	private static final Table locationScheduleTable = TablesManager.getTable("location_schedule");

	
	public static String getLocationId(String locationName) {
		DataFrame<String> resultDf = locationTable.searchEqualValues("location_name", locationName);
		int locationIdIndex = locationTable.getColumnIndex("location_id");
		return resultDf.get(0, locationIdIndex);
	}
	
	public static String getLocationName(String locationId) {
		DataFrame<String> resultDf = locationTable.searchEqualValues("location_id", locationId);
		int locationNameIndex = locationTable.getColumnIndex("location_name");
		return resultDf.get(0, locationNameIndex);
	}
	
	public static TimeRange<LocalTime> getLocationSchedule(String locationName, String dayName) {
		String locationIdString = LocationQueries.getLocationId(locationName);
		String openDay = dayName.toLowerCase() + "_open_time";
		String closeDay = dayName.toLowerCase() + "_close_time";
		int openDayIndex = locationScheduleTable.getColumnIndex(openDay);
		int closeDayIndex = locationScheduleTable.getColumnIndex(closeDay);
		String[] query = {"location_id", locationIdString};
		DataFrame<String> resultDf = locationScheduleTable.searchEqualValues(query);
		String openHour = resultDf.get(0, openDayIndex);
		String closeHour = resultDf.get(0, closeDayIndex);
		TimeRange<LocalTime> locationDaySchedule = new TimeRange<LocalTime>(openHour, closeHour);
		return locationDaySchedule;
	}
	
	public static boolean checkValidTimeRange(TimeRange<LocalTime> rangeToCheck, String locationName, String dayName) {
		TimeRange<LocalTime> locationDaySchedule = getLocationSchedule(locationName, dayName);
		
		return TimeUtil.isInnerRangeWithinOuterRange(
				locationDaySchedule.getStart(), 
				locationDaySchedule.getEnd(), 
				rangeToCheck.getStart(), 
				rangeToCheck.getEnd());
	}
}
