package prov;



public class AdminInterface extends ConsoleInterface {
    public static void main(String[] args) {
        mainMenu();
    }
    
    protected AdminInterface() {}
    
    public static void mainMenu() {
		try {
			TablesManager.loadTables();
		} catch (Exception e) {
			e.printStackTrace();
		}
        enter();
        System.out.println("ADMIN MENU");
        System.out.println("1.Add Car");
        System.out.println("2.Delete Car");
        System.out.println("0.Exit");
        enter();
        switch (read()) {
            case "1": {
                addCarMenu();
                break;
            }
            case "2": {
                deleteCarMenu();
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
    
    public static void addCarMenu() {
        String[] carAttributes = new String[9];
        String[] prompts = {
            "Enter car_id:",
            "Enter status_id:",
            "Enter location_id:",
            "Enter category_id:",
            "Enter brand:",
            "Enter model:",
            "Enter plate:",
            "Enter color:",
            "Enter transmission:"
        };

        for (int i = 0; i < prompts.length; i++) {
        	System.out.println(TablesManager.getTable("car_status").getStringedDataFrame());
        	System.out.println(TablesManager.getTable("car_category").getStringedDataFrame());
        	System.out.println(TablesManager.getTable("location").getStringedDataFrame());
            System.out.println(prompts[i]);
            carAttributes[i] = read();
            enter();
        }
        TablesManager.getTable("car").addRow(carAttributes);
        System.out.println("Car Added! Press any key to return to main menu");
        read();
        mainMenu();
    }

    public static void deleteCarMenu() {
        System.out.println("Enter the car_id to delete:");
        System.out.println(TablesManager.getTable("car").getStringedDataFrame());
        String carId = read();
        enter();
        TablesManager.getTable("car").deleteIdRow(carId);;

        System.out.println("Car with ID " + carId + " has been deleted.");
        read();
        mainMenu();
    }
}

