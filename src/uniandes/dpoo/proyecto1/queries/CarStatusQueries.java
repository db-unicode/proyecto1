package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class CarStatusQueries {
	private static final Table carStatusTable = TablesManager.getTable("car_status");

	private CarStatusQueries() {
	}
	
	public static String getCarStatusId(String carStatusName) {
		DataFrame<String> resultDf = carStatusTable.searchEqualValues("car_status_name", carStatusName);
		int carStatusIdIndex = carStatusTable.getColumnIndex("car_status_id");
		return resultDf.get(0, carStatusIdIndex);
	}
	
	public static List<String> getAllStatusName() {
		return carStatusTable.getAllColumn("car_status_name");
	}
}
