package legacy;



import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class AdminLocal extends User {

	public AdminLocal(String userId, String userType, String userRole) {
		super(userId, userType, userRole);
	}
	
	public void addUser(String userId, String userType, String password, String userRole) {
		Table userTable = TablesManager.getTable("user");
		Table userTypeTable = TablesManager.getTable(userType);
		String columnName = userType + "_id";
		userTypeTable.searchEqualValues(columnName, userId);
		userTable.addRow(userId, userType, password, userRole);
	}
	
	public void addClient() {
		
	}
	
    public void addDriverLicense(String driverLicenseId, String driverName, String expedetionCountry, String expirationDate) {
    	Table licenseTable = TablesManager.getTable("driver_license");
    	licenseTable.addRow(driverLicenseId, driverName, expedetionCountry, expirationDate);
    }
}
