package prov;

import uniandes.dpoo.proyecto1.table.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import prov.TimeRange;

public class ClientInterface extends ConsoleInterface {
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
        System.out.println("CLIENT MENU");
        System.out.println("1.Reserve Car");
        System.out.println("0.Exit");
        enter();
        switch (read()) {
            case "1": {
                try {
					reserveCarMenu();
				} catch (Exception e) {
					e.printStackTrace();
				}
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

    public static void reserveCarMenu() throws Exception {

    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

        System.out.println("Enter client ID:");
        String clientId = read();

        System.out.println(TablesManager.getTable("car_category").getStringedDataFrame());
        System.out.println("Enter desired car category name:");
        String desiredCarCategory = read();

        System.out.println(TablesManager.getTable("location").getStringedDataFrame());
        System.out.println("Enter delivery location name:");
        String deliveryLocation = read();
        
        Rental newRental = new Rental(clientId, desiredCarCategory, deliveryLocation);
        System.out.println("Enter delivery date (format: YYYY_MM_DD):");
        String deliveryDateString = read();
        LocalDate deliveryDate = LocalDate.parse(deliveryDateString, formatter);
        

        System.out.println("Enter delivery start hour (format: HH:MM):");
        String deliveryStartHour = read();

        System.out.println("Enter delivery end hour (format: HH:MM):");
        String deliveryEndHour = read();

        System.out.println("Enter return date (format: YYYY_MM_DD):");
        String returnDateString = read();
        LocalDate returnDate = LocalDate.parse(returnDateString, formatter);

        System.out.println("Enter return start hour (format: HH:MM):");
        String returnStartHour = read();

        System.out.println("Enter return end hour (format: HH:MM):");
        String returnEndHour = read();
        
        TimeRange<LocalTime> deliveryRange = new TimeRange<LocalTime>(deliveryStartHour, deliveryEndHour);
        
        TimeRange<LocalTime> returnRange = new TimeRange<LocalTime>(returnStartHour, returnEndHour);
        enter();
        newRental.setDeliveryDayTime(deliveryDate, deliveryRange);
        newRental.setReturnDayTime(returnDate, returnRange);
        newRental.saveRental();
        System.out.println("Rental saved! enter to continue");
        read();
        mainMenu();
        
    }
}
