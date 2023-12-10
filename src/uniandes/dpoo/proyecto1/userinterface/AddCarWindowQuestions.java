package uniandes.dpoo.proyecto1.userinterface;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import uniandes.dpoo.proyecto1.queries.CarCategoryQueries;
import uniandes.dpoo.proyecto1.queries.CarQueries;
import uniandes.dpoo.proyecto1.queries.CarStatusQueries;
import uniandes.dpoo.proyecto1.queries.LocationQueries;
import uniandes.dpoo.proyecto1.queries.VehicleTypeQueries;




public class AddCarWindowQuestions extends JPanel {
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;

	private NotInValuesInput carIdInput;
	private NotInValuesInput brandInput;
	private NotInValuesInput modelInput;
	private NotInValuesInput plateInput;
	private NotInValuesInput colorInput;
	private JComboBox<String> carStatusNameInput;
	private JComboBox<String> locationNameInput;
	private JComboBox<String> carCategoryInput;
	private JComboBox<String> transmissionInput;
	

	private JButton searchButton;
	private JDialog parentDialog;

	public AddCarWindowQuestions(Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(5, 2));

		buildCarInfo();
		buildCreateNewCarButton();
	}

	private void buildCarInfo() {
		JLabel carIdLabel = new JLabel("Car Id: ");
		carIdInput = new NotInValuesInput(CarQueries.getAllCarId());
		LabelInputPanel carIdPanel = new LabelInputPanel(carIdLabel, carIdInput);

		JLabel brandLabel = new JLabel("Brand: ");
		brandInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel brandPanel = new LabelInputPanel(brandLabel, brandInput);

		JLabel modelLabel = new JLabel("Model: ");
		modelInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel modelPanel = new LabelInputPanel(modelLabel, modelInput);

		JLabel plateLabel = new JLabel("Plate: ");
		plateInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel platePanel = new LabelInputPanel(plateLabel, plateInput);

		JLabel colorLabel = new JLabel("Color: ");
		colorInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel colorPanel = new LabelInputPanel(colorLabel, colorInput);
		
		JLabel carStatusNameLabel = new JLabel("Car Status: ");
		carStatusNameInput = new JComboBox<String>();
		for (String statusName: CarStatusQueries.getAllStatusName()) {
			carStatusNameInput.addItem(statusName);
		}
		LabelInputPanel carStatusNamePanel = new LabelInputPanel(carStatusNameLabel, carStatusNameInput);
		
		JLabel locationNameLabel = new JLabel("Location: ");
		locationNameInput = new JComboBox<String>();
		for (String locationName: LocationQueries.getAllLocationNames()) {
			locationNameInput.addItem(locationName);
		}
		LabelInputPanel locationNamePanel = new LabelInputPanel(locationNameLabel, locationNameInput);
		
		JLabel carCategoryLabel = new JLabel("Car Category: ");
		carCategoryInput = new JComboBox<String>();
		for (String categoryName: CarCategoryQueries.getAllCarCategorys()) {
			carCategoryInput.addItem(categoryName);
		}
		LabelInputPanel carCategoryPanel = new LabelInputPanel(carCategoryLabel, carCategoryInput);
		
		JLabel transmissionLabel = new JLabel("Vehicle Type: ");
		transmissionInput = new JComboBox<String>();
		for(String vehicleType: VehicleTypeQueries.getAllVehicleTypeNames()) {
			transmissionInput.addItem(vehicleType);
		}
		LabelInputPanel transmissionPanel = new LabelInputPanel(transmissionLabel, transmissionInput);
		

		this.add(carIdPanel);
		this.add(brandPanel);
		this.add(modelPanel);
		this.add(platePanel);
		this.add(colorPanel);
		this.add(carStatusNamePanel);
		this.add(locationNamePanel);
		this.add(carCategoryPanel);
		this.add(transmissionPanel);
	}

	private void buildCreateNewCarButton() {
		searchButton = new JButton("Add Car");
		searchButton.addActionListener(getCreateNewCarListener());
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchButtonPanel.add(searchButton);
		this.add(searchButtonPanel);
	}

	private ActionListener getCreateNewCarListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFieldFilled()) {
					try {
						String carId = carIdInput.getText();
						String brand = brandInput.getText();
						String model = modelInput.getText();
						String plate = plateInput.getText();
						String color = colorInput.getText();
						
						String carStatusName = (String) carStatusNameInput.getSelectedItem();
						String carStatusId = CarStatusQueries.getCarStatusId(carStatusName);
						
						String locationName = (String) locationNameInput.getSelectedItem();
						String locationId = LocationQueries.getLocationId(locationName);
						
						String carCategoryName = (String) carCategoryInput.getSelectedItem();
						String carCategoryId = CarCategoryQueries.getCarCategoryId(carCategoryName);
						
						String transmission = (String) transmissionInput.getSelectedItem();
						

						String[] carInfo = {carId, carStatusId, locationId, carCategoryId, brand, model, plate, color, transmission};
						CarQueries.addCar(carInfo);

						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Car Created!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Car Created!",
								JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(AddCarWindowQuestions.this, "Error: " + ex.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(AddCarWindowQuestions.this, "Please enter the values",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !carIdInput.getText().trim().isEmpty() && !brandInput.getText().trim().isEmpty()
				&& !modelInput.getText().trim().isEmpty() && !plateInput.getText().trim().isEmpty()
				&& !colorInput.getText().trim().isEmpty() && carStatusNameInput.getSelectedItem() != null && locationNameInput.getSelectedItem() != null && carCategoryInput.getSelectedItem() != null && transmissionInput.getSelectedItem() != null ;
	}

	private class NotInValuesInput extends ValidatingTrimmedTextField {
		public NotInValuesInput(List<String> validStrings) {
			super(validStrings);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void focusLost(FocusEvent e) {
			String text = getText().trim();
			if (text.isEmpty()) {
				return;
			}
			if (!validStrings.contains(text)) {
			} else {
				setText("");
				JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String datePattern = "yyyy_MM_dd";
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}
			return "";
		}
	}
}
