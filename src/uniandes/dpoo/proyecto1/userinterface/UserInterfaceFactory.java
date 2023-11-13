package uniandes.dpoo.proyecto1.userinterface;

import uniandes.dpoo.proyecto1.queries.EmployeeQueries;
import uniandes.dpoo.proyecto1.queries.UserQueries;

public class UserInterfaceFactory {

	public static void launchUserInterface(String userId, String password) {
		String userType = UserQueries.getUserType(userId, password);
		String roleName = UserQueries.getUserRoleName(userId, password);
		UserSession.setRoleName(roleName);
		UserSession.setUserType(userType);
		switch (roleName) {
		case "client":
			new ClientInterface();
			break;
		case "employee":
			UserSession.setEmployeeLocationId(EmployeeQueries.getEmployeeLocationId(userId));
			new EmployeeInterface();
			break;
		case "local_admin":
			UserSession.setEmployeeLocationId(EmployeeQueries.getEmployeeLocationId(userId));
			new LocalAdminInterface();
			break;
		case "general_admin":
			new GeneralAdminInterface();
			break;
		default:
			break;
		}
	}
}
