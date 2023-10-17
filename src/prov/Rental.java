package prov;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.lang.String;



import joinery.DataFrame;
import prov.TablesManager;
import uniandes.dpoo.proyecto1.table.Table;

public class Rental {
	private final int RELOCATION_PRICE = 30;
	
	private int rentalId;
	private String clientId;
	private int desiredCarCategoryId;
	private String originalDeliveryLocationId;
	private String originalDeliveryLocation;
	private String deliveryLocationId;
	private String deliveryLocation;
	private String carId;
	private boolean changedDesiredLocation = false;
	private String rentalStatusId = "0";
	private LocalDate deliveryDate;
	private TimeRange<LocalTime> deliveryRange;
	private LocalDate returnDate;
	private TimeRange<LocalTime> returnRange;
	private String seasonName;
	private ArrayList<String> insuranceNames = new ArrayList<String>();
	private int costPerDay = 0;
	private int totalDays;
	private int percentagePaid = 0;
	private int calculatedTotalCost = 0;
	
	public Rental(String clientId, String desiredCarCategory, String deliveryLocation) throws Exception {
		this.clientId = checkClientId(clientId);
		this.desiredCarCategoryId = checkDesiredCarCategory(desiredCarCategory);
		this.deliveryLocationId = checkDeliveryLocationId(deliveryLocation);
		this.deliveryLocation = checkDeliveryLocation(deliveryLocation);
		this.originalDeliveryLocationId = deliveryLocationId;
		this.originalDeliveryLocation = deliveryLocation;
		this.carId = searchIdealCarId();
		changeCarStatusTo("3");
		
	}
	
	private String checkClientId(String clientId) {
		Table clientTable = TablesManager.getTable("client");
		String[] query = {"client_id", clientId};
		DataFrame<String> result = clientTable.searchEqualValues(query);
		if(result.isEmpty()) {
			throw new IllegalArgumentException("Client ID not found"); 
		}
		return clientId;
	}
	
	private int checkDesiredCarCategory(String desiredCarCategory) {
		Table carCategoryTable = TablesManager.getTable("car_category");
		int carCategoryIdIndex = carCategoryTable.getColumnIndex("car_category_id");
		String[] query = {"car_category_name", desiredCarCategory};
		DataFrame<String> result = carCategoryTable.searchEqualValues(query);
		if(result.isEmpty()) {
			throw new IllegalArgumentException("Car category not found"); 
		}
		int carCategoryId = Integer.parseInt(result.get(0, carCategoryIdIndex));
		return carCategoryId;
	}
	
	private String checkDeliveryLocation(String deliveryLocation) {
		Table locationTable = TablesManager.getTable("location");
		String[] query = {"location_name", deliveryLocation};
		DataFrame<String> result = locationTable.searchEqualValues(query);
		if(result.isEmpty()) {
			throw new IllegalArgumentException("Location not found"); 
		};
		return deliveryLocation;
	}
	
	private String checkDeliveryLocationId(String deliveryLocation) {
		Table locationTable = TablesManager.getTable("location");
		int locationIdIndex = locationTable.getColumnIndex("location_id");
		String[] query = {"location_name", deliveryLocation};
		DataFrame<String> result = locationTable.searchEqualValues(query);
		if(result.isEmpty()) {
			throw new IllegalArgumentException("Location not found"); 
		};
		return result.get(0, locationIdIndex);
	}
	
	private String searchIdealCarId() throws Exception {
		Table carTable = TablesManager.getTable("car");
		int carIdIndex = carTable.getColumnIndex("car_id");
		int carCategoryIdIndex = carTable.getColumnIndex("category_id");
		int locationIdIndex = carTable.getColumnIndex("location_id");
		int carStatusIdIndex = carTable.getColumnIndex("status_id");
		DataFrame<String> carStringedDataFrame = carTable.getStringedDataFrame();
        DataFrame<String> resultWithoutLocation = carStringedDataFrame.select(row -> {
            int value = Integer.parseInt((String) row.get(carCategoryIdIndex));  
            return value >= this.desiredCarCategoryId &&
            		row.get(carStatusIdIndex).equals(getAvaibleStatusId());
        });
        resultWithoutLocation = resultWithoutLocation.sortBy((a, b) -> {
            Integer valorA = Integer.parseInt((String) a.get(1));
            Integer valorB = Integer.parseInt((String) b.get(1));
            return valorA.compareTo(valorB);
        });
        DataFrame<String> resultWithLocation = resultWithoutLocation.select(row -> row.get(locationIdIndex).equals(this.deliveryLocationId));
        if(!resultWithLocation.isEmpty()) {
        	return resultWithLocation.get(0, carIdIndex);
        } 
        if(!resultWithoutLocation.isEmpty()) {
        	this.changedDesiredLocation = true;
        	this.deliveryLocationId = resultWithoutLocation.get(0, locationIdIndex);
        	this.deliveryLocation = LocationQueries.getLocationName(deliveryLocationId);
        	return resultWithoutLocation.get(0, carIdIndex);
        }
        throw new Exception("No car found in any of our location");
	}
        
    private String getAvaibleStatusId() {
    	Table carStatusTable = TablesManager.getTable("car_status");
    	int carStatusIdIndex = carStatusTable.getColumnIndex("car_status_id");
    	String[] query = {"car_status_name", "available"};
    	DataFrame<String> result = carStatusTable.searchEqualValues(query);
    	return result.get(0, carStatusIdIndex);
    }

    private void changeCarStatusTo(String statusId) {
    	Table carTable = TablesManager.getTable("car");
    	String[] query = {"car_id", this.carId};
    	DataFrame<String> result = carTable.searchEqualValues(query);
    	carTable.changeRowColumnValue(result, "status_id", statusId);
    	
    }
    
    public void setDeliveryDayTime(LocalDate deliveryDay, TimeRange<LocalTime> deliveryRange) throws Exception {
    	String dayName = TimeUtil.getDayOfWeek(deliveryDay);
    	TimeRange<LocalTime> locationSchedule = LocationQueries.getLocationSchedule(deliveryLocation, dayName);
    	if(!TimeUtil.isInnerRangeWithinOuterRange(locationSchedule.getStart(), locationSchedule.getEnd(), deliveryRange.getStart(), deliveryRange.getEnd())) {
    		String msg = "Invalid Time Range for " + dayName + "for location " + this.deliveryLocation;
    		throw new Exception(msg);
    	}
    	this.deliveryDate = deliveryDay;
    	this.deliveryRange = deliveryRange;
    	this.seasonName = SeasonQueries.getSeason(deliveryDay);
    	this.costPerDay += SeasonQueries.getSeasonCostPerDay(String.valueOf(desiredCarCategoryId), seasonName);
    }
    
    public void setReturnDayTime(LocalDate returnDay, TimeRange<LocalTime> returnRange) throws Exception {
    	String dayName = TimeUtil.getDayOfWeek(returnDay);
    	TimeRange<LocalTime> locationSchedule = LocationQueries.getLocationSchedule(deliveryLocation, dayName);
    	if(!TimeUtil.isInnerRangeWithinOuterRange(locationSchedule.getStart(), locationSchedule.getEnd(), returnRange.getStart(), returnRange.getEnd())) {
    		String msg = "Invalid Time Range for " + dayName + "for location " + this.deliveryLocation;
    		throw new Exception(msg);
    	}
    	this.returnDate = returnDay;
    	this.returnRange = returnRange;
    	calculateTotalDays();
    }
    
    public void acceptRelocationToDesiredLocation() {
    	if(!this.changedDesiredLocation) {
    		return;
    	}
    	this.deliveryLocation = this.originalDeliveryLocation;
    	this.deliveryLocationId = this.originalDeliveryLocationId; 
    	this.costPerDay += RELOCATION_PRICE;
    }

	public boolean getChangedDesiredLocation() {
		return changedDesiredLocation;
	}
	
	public void addInsurance(String insuranceName) throws Exception {
		Table insuranceTable = TablesManager.getTable("insurance");
		int costPerDay = insuranceTable.getColumnIndex("insurance_cost_per_day");
		String[] query = {"insurance_name", insuranceName};
		DataFrame<String> result = insuranceTable.searchEqualValues(query);
		if(result.isEmpty()) {
			throw new Exception("No insurance with name " + insuranceName);
		}
		this.insuranceNames.add(insuranceName);
		this.costPerDay += Integer.parseInt(result.get(0, costPerDay));
	}
	
	public void calculateTotalDays() throws Exception {
		if (!(this.deliveryDate != null && this.returnDate != null)) {
			throw new Exception("No delivery and return info to calculate days");
		}
		this.totalDays = TimeUtil.calculateDaysBetween(deliveryDate, deliveryRange.getStart(), returnDate, returnRange.getEnd());
	}
	
	public void paidAPercentage(int percentage) throws Exception {
		if(percentage <= 0 || percentage >= 100) {
			throw new Exception("Percentage '" + percentage +"' is not valid");
		}
		this.paidAPercentage(percentage);
	}
	
	public void calculateTotalCost() {
		this.calculatedTotalCost = totalDays * costPerDay;
	}
	
	public void setRentalId() {
		this.rentalId = this.hashCode();
	}
	
	public void setRentalStatus(String statusName) throws Exception {
		Table rentalStatusTable = TablesManager.getTable("rental_status");
		int rentalStatusIdIndex = rentalStatusTable.getColumnIndex("rental_status_id");
		DataFrame<String> result = rentalStatusTable.searchEqualValues("rental_status_name", statusName);
		if (result.isEmpty()) {
			throw new Exception("Status with name '" + statusName +"' is not valid");
		}
		this.rentalStatusId = result.get(0, rentalStatusIdIndex);
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(
        		clientId,
        		desiredCarCategoryId,
        		deliveryLocationId,
        		originalDeliveryLocationId,
        		carId,
        		deliveryDate,
        		deliveryRange,
        		returnDate,
        		returnRange,
        		insuranceNames
        		);
    }
    
    public void addDriver(String driverLicenseId) {
    	Table licenseTable = TablesManager.getTable("driver_license");
    	Table rentalDrivers = TablesManager.getTable("rental_drivers");
    	@SuppressWarnings("unused")
		DataFrame<String> result = licenseTable.searchEqualValues("driver_license_id", driverLicenseId);
    	rentalDrivers.addRow(String.valueOf(rentalId), driverLicenseId);
    }
    
    private void saveInsuranceRelation() {
    	Table rentalInsuranceTable = TablesManager.getTable("rental_insurance");
    	for(String insuranceName: this.insuranceNames) {
    		rentalInsuranceTable.addRow(insuranceName, String.valueOf(rentalId));
    	}
    }
    
    public void saveRental() {
    	this.setRentalId();
    	this.calculateTotalCost();
    	this.saveInsuranceRelation();
    	Table rentalTable = TablesManager.getTable("rental");
    	rentalTable.addRow(this.rentalToRow());
    }
    
    public String[] rentalToRow() {
    	String[] rentalRow = {
    			 String.valueOf(rentalId),
    			 seasonName,
    			 String.valueOf(desiredCarCategoryId),
    			 carId,
    			 clientId,
    			 rentalStatusId,
    			 deliveryLocationId,
    			 deliveryRange.getStartAsString(),
    			 deliveryRange.getEndAsString(),
    			 deliveryDate.toString(),
    			 returnRange.getStartAsString(),
    			 returnRange.getEndAsString(),
    			 returnDate.toString(),
    			 String.valueOf(calculatedTotalCost),
    			 String.valueOf(costPerDay),
    			 String.valueOf(percentagePaid),
    			 String.valueOf(totalDays)
    	};
    	return rentalRow;
    }
}
