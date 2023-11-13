package uniandes.dpoo.proyecto1.userinterface;



public class UserSession {
	private static String userId;
	private static String userType;
	private static String roleName;
	private static String employeeLocationId = null;
	
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String userId) {
		UserSession.userId = userId;
	}
	public static String getUserType() {
		return userType;
	}
	public static void setUserType(String userType) {
		UserSession.userType = userType;
	}
	public static String getRoleName() {
		return roleName;
	}
	public static void setRoleName(String roleName) {
		UserSession.roleName = roleName;
	}
	public static String getEmployeeLocationId() {
		return employeeLocationId;
	}
	public static void setEmployeeLocationId(String employeeLocation) {
		UserSession.employeeLocationId = employeeLocation;
	}
}
