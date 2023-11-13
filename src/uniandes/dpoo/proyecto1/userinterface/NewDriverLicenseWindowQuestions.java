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
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import uniandes.dpoo.proyecto1.queries.DriverLicenseQueries;


public class NewDriverLicenseWindowQuestions extends JPanel {
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;
	private DriverLicenseInput driverLicenseIdInput;
	private DriverLicenseInput driverNameInput;
	private DriverLicenseInput countryNameInput;
	private UtilDateModel expirationDateModel;
	private JDatePickerImpl expirationDatePicker;
	private JDatePanelImpl expirationDatePanel;
	private JButton searchButton;
	private JDialog parentDialog;

	public NewDriverLicenseWindowQuestions(Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(3, 2));

		buildNewDriverLicenseInfo();
		buildNewDriverLicenseButton();
	}

	private void buildNewDriverLicenseInfo() {
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		JLabel driverLicenseIdLabel = new JLabel("Driver License Id: ");
		driverLicenseIdInput = new DriverLicenseInput(DriverLicenseQueries.getAllDriverLicenseId());
		LabelInputPanel driverIdPanel = new LabelInputPanel(driverLicenseIdLabel, driverLicenseIdInput);

		JLabel driverNameLabel = new JLabel("Driver Name: ");
		driverNameInput = new DriverLicenseInput(new ArrayList<String>());
		LabelInputPanel driverNamePanel = new LabelInputPanel(driverNameLabel, driverNameInput);

		JLabel countryNameLabel = new JLabel("Expedition Country: ");
		countryNameInput = new DriverLicenseInput(new ArrayList<String>());
		LabelInputPanel countryNamePanel = new LabelInputPanel(countryNameLabel, countryNameInput);
		
		JLabel expirationDateLabel = new JLabel("Expiration Date: ");
		expirationDateModel = new UtilDateModel();
		expirationDatePanel = new JDatePanelImpl(expirationDateModel, p);
		expirationDatePicker = new JDatePickerImpl(expirationDatePanel, new DateLabelFormatter());
		LabelInputPanel expirationDatePanel = new LabelInputPanel(expirationDateLabel, expirationDatePicker);
		
		this.add(driverIdPanel);
		this.add(driverNamePanel);
		this.add(countryNamePanel);
		this.add(expirationDatePanel);
	}

	private void buildNewDriverLicenseButton() {
		searchButton = new JButton("New Driver License");
		searchButton.addActionListener(getCreateNewClientListener());
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchButtonPanel.add(searchButton);
		this.add(searchButtonPanel);
	}

	private ActionListener getCreateNewClientListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFieldFilled()) {
					try {
						String driverLicenseId = driverLicenseIdInput.getText();
						String driverName = driverNameInput.getText();
						String countryName = countryNameInput.getText();

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
						String dateString = formatter.format(expirationDateModel.getValue());
						
						String[] driverLicenseInfo = {driverLicenseId , driverName, countryName, dateString};
						DriverLicenseQueries.addDriverLicense(driverLicenseInfo);
		
						
						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Driver License Created!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Driver License Created!", JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(NewDriverLicenseWindowQuestions.this,
								"Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(NewDriverLicenseWindowQuestions.this,
							"Please enter the values", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !driverLicenseIdInput.getText().trim().isEmpty() && !driverNameInput.getText().trim().isEmpty() && !countryNameInput.getText().trim().isEmpty() && expirationDateModel.getValue() != null;
	}

	private class DriverLicenseInput extends ValidatingTrimmedTextField {
		public DriverLicenseInput(List<String> validStrings) {
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
