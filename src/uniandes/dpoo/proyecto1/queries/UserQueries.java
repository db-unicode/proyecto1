package uniandes.dpoo.proyecto1.queries;


import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class UserQueries {
	private static final Table userTable = TablesManager.getTable("user");
	
	private UserQueries() {
	}
	
	public static boolean validateUserCredentials(String userId, String password) {
		String[] query = { "user_id", userId, "password", password };
		try {
			userTable.searchEqualValues(query);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static String getUserType(String userId, String password) {
		int userTypeIndex = userTable.getColumnIndex("user_type");
		String[] query = { "user_id", userId, "password", password };
		DataFrame<String> resultDf = userTable.searchEqualValues(query);
		String userType = resultDf.get(0, userTypeIndex);
		return userType;
	}
	
	public static String getUserRoleName(String userId, String password) {
		int roleNameIndex = userTable.getColumnIndex("role_name");
		String[] query = { "user_id", userId, "password", password };
		DataFrame<String> resultDf = userTable.searchEqualValues(query);
		String roleName = resultDf.get(0, roleNameIndex);
		return roleName;
	}
	
	public static void addUser(String[] userInfo) {
		userTable.addRow(userInfo);
	}
}
