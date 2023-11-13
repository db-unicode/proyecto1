package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class InsuranceQueries {
	private static final Table insuranceTable = TablesManager.getTable("insurance");

	private InsuranceQueries() {
	}

	public static int getInsuranceCostPerDay(String insuranceName) {
		int insuranceCostPerDayIndex = insuranceTable.getColumnIndex("insurance_cost_per_day");
		String[] query = { "insurance_name", insuranceName };
		DataFrame<String> result = insuranceTable.searchEqualValues(query);
		int insuranceCostPerDay = Integer.parseInt(result.get(0, insuranceCostPerDayIndex));
		return insuranceCostPerDay;
	}
	
	public static List<String> getAllInsranceNames() {
		return insuranceTable.getAllColumn("insurance_name");
	}
}
