package prov;

import uniandes.dpoo.proyecto1.table.Table;

import java.time.LocalDate;
import java.util.List;

import joinery.DataFrame;


public class SeasonQueries {
	private static final Table seasonTable = TablesManager.getTable("season");
	private static final Table seasonIntervalTable = TablesManager.getTable("season_interval");
	
	private SeasonQueries() {}
	
	public static int getSeasonCostPerDay(String carCategoryId, String seasonName) {
		String[] query = {"season_name", seasonName, "car_category_id", carCategoryId};
		DataFrame<String> resultDf = seasonTable.searchEqualValues(query);
		int costPerDayIndex = seasonTable.getColumnIndex("cost_per_day");
		int costPerDay = Integer.parseInt(resultDf.get(0, costPerDayIndex));
		return costPerDay;
	}
	
	public static String getSeason(LocalDate starDate) {
		DataFrame<String> resultDf = seasonIntervalTable.getStringedDataFrame();
		int startDateIndex = seasonIntervalTable.getColumnIndex("start_date");
		int endDateIndex = seasonIntervalTable.getColumnIndex("end_date");
		int seasonNameIndex = seasonIntervalTable.getColumnIndex("season_name");
		for (List<String> row : resultDf) {
			String startDateString = row.get(startDateIndex);
			String endDateString = row.get(endDateIndex);
			TimeRange<LocalDate> seasonInterval = new TimeRange<LocalDate>(startDateString, endDateString);
			if(TimeUtil.isBetween(starDate, seasonInterval.getStart(), seasonInterval.getEnd())) {
				return row.get(seasonNameIndex);
			}
		}
		throw new IllegalArgumentException("The provided date is not valid for any season.");
	}
}
