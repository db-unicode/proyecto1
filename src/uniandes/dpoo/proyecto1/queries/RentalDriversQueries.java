package uniandes.dpoo.proyecto1.queries;

import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class RentalDriversQueries {
	private static final Table rentalDriversTable = TablesManager.getTable("rental_drivers");

	private RentalDriversQueries() {
	}
	
    public static void addDriver(String rentalId, String driverLicenseId) {
    	DriverLicenseQueries.checkDriverLicenseId(driverLicenseId);
    	rentalDriversTable.addRow(rentalId, driverLicenseId);
    }
}
