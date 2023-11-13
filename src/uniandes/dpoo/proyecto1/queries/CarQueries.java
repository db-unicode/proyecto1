package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class CarQueries {
	private static final Table carTable = TablesManager.getTable("car");
	
	private CarQueries() {
	}
	
    public static void changeCarStatusTo(String carId, String statusId) {
    	String[] query = {"car_id", carId};
    	DataFrame<String> result = carTable.searchEqualValues(query);
    	carTable.changeRowColumnValue(result, "status_id", statusId);
    }
    
    public static String getCarLocationId(String carId) {
    	int carLocationIdIndex =  carTable.getColumnIndex("location_id");
    	String[] query = {"car_id", carId};
    	DataFrame<String> result = carTable.searchEqualValues(query);
    	String carLocationId = result.get(0, carLocationIdIndex);
    	return carLocationId;
    }
    
    public static String getCarInfo(String carId) {
    	String[] query = {"car_id", carId};
    	DataFrame<String> result = carTable.searchEqualValues(query);
    	List<String> carInfo = result.row(0);
    	String carInfoString = String.join(", ", carInfo);
    	return carInfoString;
    }
    
    public static List<String> getAllCarIdOfALocationId(String locationId) {
    	int carIdIndex =  carTable.getColumnIndex("car_id");
    	String[] query = {"location_id", locationId};
    	DataFrame<String> result = carTable.searchEqualValues(query);
    	List<String> allCarsIdOfLocation = result.col(carIdIndex);
    	return allCarsIdOfLocation;
    }
    
    public static List<String> getAllCarId() {
    	return carTable.getAllColumn("car_id");
    }
    
    public static void addCar(String[] carInfo) {
    	carTable.addRow(carInfo);
    }
    
    public static void deleteCar(String carId) {
    	String[] query = {"car_id", carId};
    	DataFrame<String> result = carTable.searchEqualValues(query);
    	carTable.deleteRow(result);
    }
    
}
