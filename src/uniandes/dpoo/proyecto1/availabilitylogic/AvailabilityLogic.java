package uniandes.dpoo.proyecto1.availabilitylogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uniandes.dpoo.proyecto1.queries.CarQueries;
import uniandes.dpoo.proyecto1.queries.LocationQueries;
import uniandes.dpoo.proyecto1.queries.RentalQueries;
import uniandes.dpoo.proyecto1.timetools.TimeRange;
import uniandes.dpoo.proyecto1.timetools.TimeUtil;

public class AvailabilityLogic {
	
	public static int countAvailableCarsInLocation(LocalDate desiredDate, String locationName) {
		String locationId = LocationQueries.getLocationId(locationName);
		List<String> allLocationCarIds = CarQueries.getAllCarIdOfALocationId(locationId);
		HashMap<String, ArrayList<TimeRange<LocalDate>>> carIdTimeRangesMap = RentalQueries.getCarMapOfRentalsRanges(allLocationCarIds);
		int noAvailable = 0;
		for (String carId : carIdTimeRangesMap.keySet()) {
		    ArrayList<TimeRange<LocalDate>> carTimeRanges = carIdTimeRangesMap.get(carId);
		    boolean deleteCarId = checkRentalCarTimeRanges(desiredDate, carTimeRanges);
		    if (deleteCarId) {
		    	noAvailable += 1;
		    }
		}
		return allLocationCarIds.size() - noAvailable;
	}
	
	private static boolean checkRentalCarTimeRanges(LocalDate desiredDate, ArrayList<TimeRange<LocalDate>> carTimeRanges) {
		if (carTimeRanges == null) {
			return false;
		}
		for(TimeRange<LocalDate> rentalRange: carTimeRanges) {
			if (TimeUtil.isBetween(desiredDate, rentalRange.getStart(), rentalRange.getEnd())) {
				return true;
			}
		}
		return false;
	}
}
