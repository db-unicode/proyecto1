package uniandes.dpoo.proyecto1.queries;

import java.util.ArrayList;

import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class RentalInsuranceQueries {
	private static final Table rentalInsuranceTable = TablesManager.getTable("rental_insurance");
	
	private RentalInsuranceQueries() {
	}
	
    public static void saveRentalInsuranceRelation(String rentalId, ArrayList<String> insuranceNames) {
    	for(String insuranceName: insuranceNames) {
    		rentalInsuranceTable.addRow(insuranceName, rentalId);
    	}
    }
}
