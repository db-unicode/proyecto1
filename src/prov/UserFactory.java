package prov;
import prov.User;
import prov.TablesManager;
import uniandes.dpoo.proyecto1.table.Table;
import joinery.DataFrame;

import java.util.List;
import java.util.function.Predicate;
import prov.User;
import prov.Client;

public class UserFactory {
	private static Table userTable = TablesManager.getTable("user");

	
	public static User createUser(String userId, String password) {
		List<String> resultRow = getUserRow(userId, password);
        if (!resultRow.isEmpty()) {
        	int userRoleIndex = UserFactory.userTable.getColumnIndex("role_name");
        	int userTypeIndex = UserFactory.userTable.getColumnIndex("user_type");
        	String userRole = resultRow.get(userRoleIndex);
        	String userType = resultRow.get(userTypeIndex);
        	return UserFactory.createUserWithRole(userId, userType, userRole);
        } else {
        	System.err.println("No user found");
        }
		return null;
	}
	
	private static List<String> getUserRow(String userId, String password) {
		int userIdIndex = UserFactory.userTable.getColumnIndex("user_id");
		int userPasswordIndex = UserFactory.userTable.getColumnIndex("password");
		DataFrame<String>  df = userTable.getStringedDataFrame();
        DataFrame<String> argumentsQuery = df.select(row -> 
        	userId.equals(row.get(userIdIndex)) && 
        	password.equals(row.get(userPasswordIndex)));
        List<String> resultRow = argumentsQuery.row(0);
        return resultRow;
	}
	
	private static User createUserWithRole(String userId, String userType, String userRole) {
		if (userRole == "cliente") {
			return new Client(userId, userType, userRole);
		}
		return null;
	}
	
}
