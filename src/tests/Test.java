package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import joinery.DataFrame;
import prov.CSVManager;
import uniandes.dpoo.proyecto1.table.Table;
import prov.JSONManager;
import prov.Rental;
import prov.TableMetadata;
import prov.TablesManager;

public class Test {
	
	public static void main(String[] args) {
		try {
			TablesManager.loadTables();
//			System.out.println(TablesManager.getTable("client").getDataFrame());
			
			System.out.println(TablesManager.getTable("employee").getDataFrame());
			System.out.println(TablesManager.getTable("user").getDataFrame());
//			System.out.println(TablesManager.getTable("rental").getColumns());
//			System.out.println(TablesManager.getTable("season").getDataFrame());
//			System.out.println(TablesManager.getTable("season_interval").getDataFrame());
			
//			TableMetadata mockMetadata = new TableMetadata("data/mock_metadata.json");
//			System.out.println(mockMetadata.getPrimaryKeys());
//			System.out.println(mockMetadata.getForeignKeys());
//			System.out.println(mockMetadata.getColumnType());
			
			Table clientTable = new Table("data/client");
			Table licenseTable = new Table("data/driver_license");
			Table creditCardTable = new Table("data/credit_card");
			Table carStatusTable = new Table("data/car_status");
			Table carCategoryTable = new Table("data/car_category");
			Table locationTable = new Table("data/location");
			Table carTable = new Table("data/car");
			Table locationScheduleTable = new Table("data/location_schedule");
			Table seasonTable = new Table("data/season");
			Table insuranceTable = new Table("data/insurance");
			Table rentalStatusTable = new Table("data/rental_status");
			Table rentalTable = new Table("data/rental");
			Table rentalDriversTable = new Table("data/rental_drivers");
			System.out.println(clientTable.getDataFrame());
			System.out.println(licenseTable.getDataFrame());
			System.out.println(creditCardTable.getDataFrame());
			System.out.println(carStatusTable.getDataFrame());
			System.out.println(carCategoryTable.getDataFrame());
			System.out.println(locationTable.getDataFrame());
			System.out.println(carTable.getDataFrame());
			System.out.println(locationScheduleTable.getDataFrame());
			System.out.println(seasonTable.getDataFrame());
			System.out.println(insuranceTable.getDataFrame());
			System.out.println(rentalStatusTable.getDataFrame());
			System.out.println(rentalTable.getStringedDataFrame());
			System.out.println(rentalDriversTable.getColumns());
			System.out.println(TablesManager.getTable("season_interval").getDataFrame());

//			Rental aRental = new Rental("1001", "deluxe", "Harbor Outlet");
//			System.out.println(mockTable.addRow("7258780612","pedro","tata@","2023-01-14","true","13").getTable());;
//			System.out.println(mockTable.getTable().col("client").get(10).getClass());
//			System.out.println(mockTable.getMetadata().getPrimaryKeys());


			
//			DataFrame<Object> table = CSVManager.loadCSV("data/mock.csv");
//			Set<Object> columnSet = table.columns();
//			List<Object> columnList = new ArrayList<>(columnSet);
//			int index = columnList.indexOf("id");
//			DataFrame<Object> result = table.select(row -> {
//				int id = ((Number) row.get(index)).intValue();
//				return id >= 5 && id <= 32; 
//			});
//			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
