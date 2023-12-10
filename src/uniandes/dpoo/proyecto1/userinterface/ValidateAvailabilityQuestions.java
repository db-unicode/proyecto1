package uniandes.dpoo.proyecto1.userinterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import uniandes.dpoo.proyecto1.availabilitylogic.AvailabilityLogic;
import uniandes.dpoo.proyecto1.queries.LocationQueries;



public class ValidateAvailabilityQuestions extends JPanel {


	private static final long serialVersionUID = 1L;
	private JDatePickerImpl desiredDatePicker;
	private JDatePanelImpl desiredDatePanel;
	private UtilDateModel desiredDateModel;
	private JComboBox<String> locationNamePicker;
	private JLabel resultLabel;
	private JButton validateAvailabilityButton;
	
	
	public ValidateAvailabilityQuestions() {
		this.setPreferredSize(new Dimension(1024, 500));
		this.setLayout(new GridLayout(2, 2));

		buildDatePickers();
		buildDesiredLocation();
		buildValidateAvailabilityButton();
		buildResultLabel();
	}
	
	private void buildDatePickers() {
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		

		JLabel desiredDateLabel = new JLabel("Date to check availability: ");
		desiredDateModel = new UtilDateModel();
		desiredDatePanel = new JDatePanelImpl(desiredDateModel, p);
		desiredDatePicker = new JDatePickerImpl(desiredDatePanel, new DateLabelFormatter());
		LabelInputPanel desiredDate = new LabelInputPanel(desiredDateLabel, desiredDatePicker);

		this.add(desiredDate);
	}
	
	private void buildDesiredLocation() {
		JLabel deliveryLocationLabel = new JLabel("Desired Location: ");
		locationNamePicker = new JComboBox<>(new Vector<>(LocationQueries.getAllLocationNames()));
		LabelInputPanel locationPanel = new LabelInputPanel(deliveryLocationLabel, locationNamePicker);
		this.add(locationPanel);
	}
	
	private void buildResultLabel() {
		resultLabel = new JLabel("			");
		JLabel resultTextLabel = new JLabel("Total cars available: ");
		LabelInputPanel resultInfo = new LabelInputPanel(resultTextLabel, resultLabel);
		this.add(resultInfo);
	} 
	
	
	private void buildValidateAvailabilityButton() {
		validateAvailabilityButton = new JButton("Validate availability");
		validateAvailabilityButton.addActionListener(getValidateAvailabilityListener());
		JPanel searchCarButtonPanel = new JPanel();
		searchCarButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchCarButtonPanel.add(validateAvailabilityButton);
		this.add(searchCarButtonPanel);
	}

	private ActionListener getValidateAvailabilityListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (allFieldsFilled()) {
					LocalDate desiredLocalDate = desiredDateModel.getValue().toInstant()
							.atZone(ZoneId.systemDefault()).toLocalDate();
					String deliveryLocation = (String) locationNamePicker.getSelectedItem();
					try {
						String totalCars = String.valueOf(AvailabilityLogic.countAvailableCarsInLocation(desiredLocalDate, deliveryLocation));
						resultLabel.setText(totalCars);
						JOptionPane.showMessageDialog(ValidateAvailabilityQuestions.this,
	                            "Number of cars available: " + totalCars, 
	                            "Info", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(ValidateAvailabilityQuestions.this,
								e2, "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(ValidateAvailabilityQuestions.this, "Some fields are empty", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}
	
	private boolean allFieldsFilled() {

	    boolean isDateSelected = desiredDateModel.getValue() != null;

	    boolean isLocationSelected = locationNamePicker.getSelectedIndex() != -1;

	    return isDateSelected && isLocationSelected;
	}

	public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

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
