package legacy;

import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class LocalAdminInterface extends ConsoleInterface {
    public static void main(String[] args) {
        mainMenu();
    }
    
    public static void mainMenu() {
        try {
            TablesManager.loadTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
        enter();
        System.out.println("LOCAL ADMIN MENU");
        System.out.println("1.Add Employee");
        System.out.println("2.Add Client");
        System.out.println("3.Add User");
        System.out.println("0.Exit");
        enter();
        switch (read()) {
            case "1": {
                addEmployeeMenu();
                break;
            }
            case "2": {
                addClientMenu();
                break;
            }
            case "3": {
                addUserMenu();
                break;
            }
            case "0": {
                System.exit(0);
                break;
            }
            default:
                System.err.println("Non valid option");
                mainMenu();
                break;
        }
    }
    
    public static void addEmployeeMenu() {
        String[] employeeAttributes = new String[4];
        String[] prompts = {
            "Enter employee_id:",
            "Enter role_id:",
            "Enter employee_name:",
            "Enter employee_location_id:"
        };

        for (int i = 0; i < prompts.length; i++) {
            System.out.println(prompts[i]);
            employeeAttributes[i] = read();
            enter();
        }
        Table employeeTable = TablesManager.getTable("employee");
        employeeTable.addRow(employeeAttributes);
    }

    public static void addClientMenu() {
        String[] clientAttributes = new String[7];
        String[] prompts = {
            "Enter client_id:",
            "Enter driver_license_id:",
            "Enter client_name:",
            "Enter birth_date: format YYYY_MM_DD",
            "Enter nationality:",
            "Enter credit_card_id:",
            "Enter contact_number:"
        };

        for (int i = 0; i < prompts.length; i++) {
            System.out.println(prompts[i]);
            clientAttributes[i] = read();
            enter();
        }
        Table clienTable = TablesManager.getTable("client");
        clienTable.addRow(clientAttributes);
    }

    public static void addUserMenu() {
        String[] userAttributes = new String[4];
        String[] prompts = {
            "Enter user_id:",
            "Enter user_type:",
            "Enter password:",
            "Enter role_name:"
        };

        for (int i = 0; i < prompts.length; i++) {
            System.out.println(prompts[i]);
            userAttributes[i] = read();
            enter();
        }
        Table userTable = TablesManager.getTable("user");
        userTable.addRow(userAttributes);
    }
}
