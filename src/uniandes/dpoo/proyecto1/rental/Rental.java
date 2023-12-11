package uniandes.dpoo.proyecto1.rental;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.lang.String;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.facturas.FacturaImpresora;
import uniandes.dpoo.proyecto1.queries.CarCategoryQueries;
import uniandes.dpoo.proyecto1.queries.CarQueries;
import uniandes.dpoo.proyecto1.queries.CarStatusQueries;
import uniandes.dpoo.proyecto1.queries.ClientQueries;
import uniandes.dpoo.proyecto1.queries.CreditCardQueries;
import uniandes.dpoo.proyecto1.queries.InsuranceQueries;
import uniandes.dpoo.proyecto1.queries.LocationQueries;
import uniandes.dpoo.proyecto1.queries.RentalDriversQueries;
import uniandes.dpoo.proyecto1.queries.RentalInsuranceQueries;
import uniandes.dpoo.proyecto1.queries.RentalStatusQueries;
import uniandes.dpoo.proyecto1.queries.SeasonQueries;
import uniandes.dpoo.proyecto1.queries.VehicleTypeQueries;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;
import uniandes.dpoo.proyecto1.timetools.TimeRange;
import uniandes.dpoo.proyecto1.timetools.TimeUtil;

public class Rental {
	private final int RELOCATION_PRICE = 30;
	private final int EXTRA_DRIVER_PRICE = 15;
	
	private String rentalId;
	private String clientId;
	private int desiredCarCategoryId;
	private String originalDeliveryLocationId;
	private String originalDeliveryLocation;
	private String deliveryLocationId;
	private String deliveryLocation;
	private String carId;
	private boolean changedDesiredLocation = false; 
	private String rentalStatusId = RentalStatusQueries.getRentalStatusId("reserved");
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

	public Rental(String clientId, String desiredCarCategoryName, String deliveryLocationName) throws Exception {
		this.clientId = ClientQueries.checkClientId(clientId);
		this.desiredCarCategoryId = CarCategoryQueries.checkDesiredCarCategory(desiredCarCategoryName);
		this.deliveryLocationId = LocationQueries.checkDeliveryLocationId(deliveryLocationName);
		this.deliveryLocation = LocationQueries.checkDeliveryLocation(deliveryLocationName);
		this.originalDeliveryLocationId = deliveryLocationId;
		this.originalDeliveryLocation = deliveryLocationName;
		this.carId = searchIdealCarId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientId, desiredCarCategoryId, deliveryLocationId, originalDeliveryLocationId, carId,
				deliveryDate, deliveryRange, returnDate, returnRange, insuranceNames);
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
			return value >= this.desiredCarCategoryId
					&& row.get(carStatusIdIndex).equals(CarStatusQueries.getCarStatusId("available"));
		});
		resultWithoutLocation = resultWithoutLocation.sortBy((a, b) -> {
			Integer valorA = Integer.parseInt((String) a.get(1));
			Integer valorB = Integer.parseInt((String) b.get(1));
			return valorA.compareTo(valorB);
		});
		DataFrame<String> resultWithLocation = resultWithoutLocation
				.select(row -> row.get(locationIdIndex).equals(this.deliveryLocationId));
		if (!resultWithLocation.isEmpty()) {
			return resultWithLocation.get(0, carIdIndex);
		}
		if (!resultWithoutLocation.isEmpty()) {
			this.changedDesiredLocation = true;
			this.deliveryLocationId = resultWithoutLocation.get(0, locationIdIndex);
			this.deliveryLocation = LocationQueries.getLocationName(deliveryLocationId);
			return resultWithoutLocation.get(0, carIdIndex);
		}
		throw new Exception("No car found in any of our location");
	}

	private void saveInsuranceRelation() {
		RentalInsuranceQueries.saveRentalInsuranceRelation(rentalId, insuranceNames);
	}

	public void setDeliveryDayTime(LocalDate deliveryDay, TimeRange<LocalTime> deliveryRange) throws Exception {
		String dayName = TimeUtil.getDayOfWeek(deliveryDay);
		TimeRange<LocalTime> locationSchedule = LocationQueries.getLocationSchedule(deliveryLocation, dayName);
		if (!TimeUtil.isInnerRangeWithinOuterRange(locationSchedule.getStart(), locationSchedule.getEnd(),
				deliveryRange.getStart(), deliveryRange.getEnd())) {
			String msg = "Invalid Time Range for " + dayName + " for location " + this.deliveryLocation;
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
		if (!TimeUtil.isInnerRangeWithinOuterRange(locationSchedule.getStart(), locationSchedule.getEnd(),
				returnRange.getStart(), returnRange.getEnd())) {
			String msg = "Invalid Time Range for " + dayName + "for location " + this.deliveryLocation;
			throw new Exception(msg);
		}
		this.returnDate = returnDay;
		this.returnRange = returnRange;
		calculateTotalDays();
	}

	public void acceptRelocationToDesiredLocation() {
		if (!this.changedDesiredLocation) {
			return;
		}
		this.deliveryLocation = this.originalDeliveryLocation;
		this.deliveryLocationId = this.originalDeliveryLocationId;
		this.calculatedTotalCost += RELOCATION_PRICE;
	}

	public void addInsurance(String insuranceName) throws Exception {
		int insuranceCostPerDay = InsuranceQueries.getInsuranceCostPerDay(insuranceName);
		String vehicleTypeName = CarQueries.getVehicleTypeId(carId);
		float floatRealInsuranceCostPerDay = insuranceCostPerDay * VehicleTypeQueries.getVehicleInsuranceAdditional(vehicleTypeName);
		int realInsuranceCostPerDay = (int) Math.ceil(floatRealInsuranceCostPerDay);
		this.insuranceNames.add(insuranceName);
		this.costPerDay += realInsuranceCostPerDay;
	}

	public void addInsuranceList(List<String> insuranceNameList) throws Exception {
		for (String insuranceName : insuranceNameList) {
			this.addInsurance(insuranceName);
		}
	}
	public void calculateTotalDays() throws Exception {
		if (!(this.deliveryDate != null && this.returnDate != null)) {
			throw new Exception("No delivery and return info to calculate days");
		}
		this.totalDays = TimeUtil.calculateDaysBetween(deliveryDate, deliveryRange.getStart(), returnDate,
				returnRange.getEnd());
	}

	public void payAPercentage(int percentage) throws Exception {
		if (percentage <= 0 || percentage >= 100) {
			throw new Exception("Percentage '" + percentage + "' is not valid");
		}
		if (CreditCardQueries.isCreditCardBlocked(ClientQueries.checkClientCreditCardId(clientId))) {
			throw new Exception("Credit Card of client '" + clientId + "' is blocked");
		}
		this.percentagePaid = percentage;
	}

	public void calculateTotalCost() {
		this.calculatedTotalCost += totalDays * costPerDay;
	}

	public void setRentalId() {
		this.rentalId = String.valueOf(hashCode());
	}

	public void addDriver(String driverLicenseId) {
		RentalDriversQueries.addDriver(rentalId, driverLicenseId);
		calculatedTotalCost += EXTRA_DRIVER_PRICE;
	}

	public void saveRental() {
		this.setRentalId();
		this.calculateTotalCost();
		this.saveInsuranceRelation();
		CarQueries.changeCarStatusTo(carId, CarStatusQueries.getCarStatusId("reserved"));
		Table rentalTable = TablesManager.getTable("rental");
		rentalTable.addRow(this.rentalToRow());
        try {
            FacturaImpresora.generarFacturaPDF(ClientQueries.getClientName(clientId), CarQueries.getCarInfo(carId), deliveryLocation, String.valueOf(calculatedTotalCost));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public String[] rentalToRow() {
		String[] rentalRow = { String.valueOf(rentalId), seasonName, String.valueOf(desiredCarCategoryId), carId,
				clientId, rentalStatusId, deliveryLocationId, deliveryRange.getStartAsString(),
				deliveryRange.getEndAsString(), deliveryDate.toString(), returnRange.getStartAsString(),
				returnRange.getEndAsString(), returnDate.toString(), String.valueOf(calculatedTotalCost),
				String.valueOf(costPerDay), String.valueOf(percentagePaid), String.valueOf(totalDays) };
		return rentalRow;
	}

	public boolean isChangedDesiredLocation() {
		return changedDesiredLocation;
	}

	public String getCarId() {
		return carId;
	}
	
	public String getDeliveryLocation() {
		return deliveryLocation;
	}

	public int getCostPerDay() {
		return costPerDay;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public int getCalculatedTotalCost() {
		calculateTotalCost();
		return calculatedTotalCost;
	}
}
