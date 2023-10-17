package prov;

import java.util.List;

import joinery.DataFrame;
import prov.TablesManager;
import uniandes.dpoo.proyecto1.table.Table;


public class User {
	private String userId; 
	private String userType;
	private String userRole;
	private List<String> userInfo;
	
	public User(String userId, String userType, String userRole) {
		this.userId = userId;
		this.userType = userType;
		this.userRole = userRole;
		this.userInfo = this.getUserInfo(userId, userType);
	}
	
	private List<String> getUserInfo(String userId, String userType) {
		String fixedIdColumnName = userType + "_id";
		Table userTypeTable = TablesManager.getTable(userType);
		int userIdIndex = userTypeTable.getColumnIndex(fixedIdColumnName);
		DataFrame<String> df = userTypeTable.getStringedDataFrame();
        DataFrame<String> argumentsQuery = df.select(row -> 
        	userId.equals(row.get(userIdIndex)));
        List<String> resultRow = argumentsQuery.row(0);
        return resultRow;
	}
	
	// change to protected
	public void addToTableValues(String tableName, String[] values) {
		Table table = TablesManager.getTable(tableName);
		table.addRow(values);
	}

}
