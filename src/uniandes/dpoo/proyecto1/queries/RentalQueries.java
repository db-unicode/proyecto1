package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class RentalQueries {
	private static final Table rentalTable = TablesManager.getTable("rental");

	private RentalQueries() {
	}
	
	public static List<String> getAllRentalOfLocationIdWithStatus(String locationId, String statusId) {
		String[] query = { "delivery_location_id", locationId, "rental_status_id", statusId};
		int rentalIdIndex = rentalTable.getColumnIndex("rental_id");
		DataFrame<String> result = rentalTable.searchEqualValues(query);
		List<String> rentalIdList = result.col(rentalIdIndex);
		return rentalIdList;
	}

	
	
	
	public static List<String> getAllRentalId() {
		return rentalTable.getAllColumn("rental_id");
	}
	
	public static String getCarId(String rentalId) {
		String[] query = { "rental_id", rentalId };
		int carIdIndex = rentalTable.getColumnIndex("car_id");
		DataFrame<String> result = rentalTable.searchEqualValues(query);
		String carId = result.get(0, carIdIndex);
		return carId;
	}
	
    public static String getRentalInfo(String rentalId) {
    	String[] query = { "rental_id", rentalId };
		DataFrame<String> result = rentalTable.searchEqualValues(query);
		List<String> rentalInfoList = result.row(0);
    	String rentalInfoString = String.join(", ", rentalInfoList);
    	return rentalInfoString;
    }
    
	public static void sumDriverToTotalCost(String rentalId) {
		String[] query = { "rental_id", rentalId };
		int calculatedCostIndex = rentalTable.getColumnIndex("calculated_cost");
		DataFrame<String> result = rentalTable.searchEqualValues(query);
		String calculatedCostString = result.get(0, calculatedCostIndex);
		int calculatedCostInt = Integer.parseInt(calculatedCostString);
		int newCalculatedCost = calculatedCostInt + 15;
		rentalTable.changeRowColumnValue(result, "calculated_cost", String.valueOf(newCalculatedCost));
	}
	
    public static void changeRentalStatusTo(String rentalId, String statusId) {
    	String[] query = {"rental_id", rentalId};
    	DataFrame<String> result = rentalTable.searchEqualValues(query);
    	rentalTable.changeRowColumnValue(result, "rental_status_id", statusId);
    }
}
