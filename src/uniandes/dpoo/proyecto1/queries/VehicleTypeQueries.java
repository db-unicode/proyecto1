package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class VehicleTypeQueries {
	private static final Table vehicleTypeTable = TablesManager.getTable("vehicle_type");

	private VehicleTypeQueries() {
	}
	
	public static float getVehicleInsuranceAdditional(String vehicleTypeName) {
		String[] query = {"type_name", vehicleTypeName};
		int insuranceAdditionalIndex = vehicleTypeTable.getColumnIndex("insurance_additional");
		DataFrame<String> result = vehicleTypeTable.searchEqualValues(query);
		String stringInsuranceAdditional = result.get(0, insuranceAdditionalIndex);
		int intInsuranceAdditional = Integer.parseInt(stringInsuranceAdditional);
		float insuranceAdditionalPercentege = intInsuranceAdditional / 100.0f;
		return insuranceAdditionalPercentege;
	}
	
	public static List<String> getAllVehicleTypeNames() {
		return vehicleTypeTable.getAllColumn("type_name");
	}
}
