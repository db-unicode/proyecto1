package uniandes.dpoo.proyecto1.queries;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class RentalStatusQueries {
	private static final Table rentalStatusTable = TablesManager.getTable("rental_status");

	private RentalStatusQueries() {
	}
	
	public static String getRentalStatusId(String rentalStatusName) {
		DataFrame<String> resultDf = rentalStatusTable.searchEqualValues("rental_status_name", rentalStatusName);
		int rentalStatusIdIndex = rentalStatusTable.getColumnIndex("rental_status_id");
		return resultDf.get(0, rentalStatusIdIndex);
	}
}
