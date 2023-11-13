package uniandes.dpoo.proyecto1.queries;


import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class DriverLicenseQueries {
	private static final Table driverLicenseTable = TablesManager.getTable("driver_license");

	private DriverLicenseQueries() {
	}

	public static void checkDriverLicenseId(String driverLicenseId) {
		driverLicenseTable.searchEqualValues("driver_license_id", driverLicenseId);
	}
	
	public static List<String> getAllDriverLicenseId() {
		return driverLicenseTable.getAllColumn("driver_license_id");
	}
	
	public static String getDriverName(String driverLicenseId) {
		DataFrame<String> resultDataFrame = driverLicenseTable.searchEqualValues("driver_license_id", driverLicenseId);
		int driverNameIndex = driverLicenseTable.getColumnIndex("driver_name");
		String driverName = resultDataFrame.get(0, driverNameIndex);
		return driverName;
	}
	
	public static void addDriverLicense(String[] driverLicenseInfo) {
		driverLicenseTable.addRow(driverLicenseInfo);
	}

}
