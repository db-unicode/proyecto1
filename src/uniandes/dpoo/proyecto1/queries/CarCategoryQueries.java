package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class CarCategoryQueries {
	private static final Table carCategoryTable = TablesManager.getTable("car_category");
	
	private CarCategoryQueries() {
	}

	public static int checkDesiredCarCategory(String desiredCarCategory) {
		int carCategoryIdIndex = carCategoryTable.getColumnIndex("car_category_id");
		String[] query = { "car_category_name", desiredCarCategory };
		DataFrame<String> result = carCategoryTable.searchEqualValues(query);
		int carCategoryId = Integer.parseInt(result.get(0, carCategoryIdIndex));
		return carCategoryId;
	}
	
	public static List<String> getAllCarCategorys() {
		return carCategoryTable.getAllColumn("car_category_name");
	} 
	
	public static String getCarCategoryId(String carCategoryName) {
		int carCategoryIdIndex = carCategoryTable.getColumnIndex("car_category_id");
		String[] query = { "car_category_name", carCategoryName };
		DataFrame<String> result = carCategoryTable.searchEqualValues(query);
		String carCategoryId = result.get(0, carCategoryIdIndex);
		return carCategoryId;
	}
}
