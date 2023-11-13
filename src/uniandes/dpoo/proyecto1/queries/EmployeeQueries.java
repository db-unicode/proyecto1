package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class EmployeeQueries {
	private static final Table employeeTable = TablesManager.getTable("employee");

	private EmployeeQueries() {
	}
	
	public static String getEmployeeLocationId(String employeeId) {
		String[] query = { "employee_id", employeeId };
		int employeeLocationIdIndex = employeeTable.getColumnIndex("employee_location_id");
		DataFrame<String> result = employeeTable.searchEqualValues(query);
		String employeeLocationId = result.get(0, employeeLocationIdIndex);
		return employeeLocationId;
	}
	
	public static List<String> getAllEmpoyeeIds() {
		return employeeTable.getAllColumn("employee_id");
	}
	
	public static List<String> getAllEmpoyeeNames() {
		return employeeTable.getAllColumn("employee_name");
	}
	
	public static void addEmployee(String[] employeeInfo) {
		employeeTable.addRow(employeeInfo);
	}
}
