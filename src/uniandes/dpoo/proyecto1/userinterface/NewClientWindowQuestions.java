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

import uniandes.dpoo.proyecto1.queries.ClientQueries;
import uniandes.dpoo.proyecto1.queries.CreditCardQueries;
import uniandes.dpoo.proyecto1.queries.DriverLicenseQueries;
import uniandes.dpoo.proyecto1.queries.UserQueries;

public class NewClientWindowQuestions extends JPanel {
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;

	private NotInValuesInput clientIdInput;
	private NotInValuesInput clientNameInput;
	private NotInValuesInput nationalityInput;
	private NotInValuesInput contactNumberInput;
	private UtilDateModel birthDateModel;
	private JDatePickerImpl birthDatePicker;
	private JDatePanelImpl birthDatePanel;
	private InValuesInput creditCardIdInput;
	private InValuesInput driverLicenseIdInput;
	private NotInValuesInput passwordInput;

	private JButton searchButton;
	private JDialog parentDialog;

	public NewClientWindowQuestions(Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(4, 2));

		buildNewClientInfo();
		buildCreateNewClientButton();
	}

	private void buildNewClientInfo() {
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");

		JLabel clientIdLabel = new JLabel("Client Id: ");
		clientIdInput = new NotInValuesInput(ClientQueries.getAllClientId());
		LabelInputPanel clientIdPanel = new LabelInputPanel(clientIdLabel, clientIdInput);

		JLabel clientNameLabel = new JLabel("Client Name: ");
		clientNameInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel clientNamePanel = new LabelInputPanel(clientNameLabel, clientNameInput);

		JLabel nationalityLabel = new JLabel("Nationality: ");
		nationalityInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel nationalityPanel = new LabelInputPanel(nationalityLabel, nationalityInput);

		JLabel contactNumberLabel = new JLabel("Contact Number: ");
		contactNumberInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel contactNumberPanel = new LabelInputPanel(contactNumberLabel, contactNumberInput);

		JLabel birthDateLabel = new JLabel("Birth Date: ");
		birthDateModel = new UtilDateModel();
		birthDatePanel = new JDatePanelImpl(birthDateModel, p);
		birthDatePicker = new JDatePickerImpl(birthDatePanel, new DateLabelFormatter());
		LabelInputPanel expirationDatePanel = new LabelInputPanel(birthDateLabel, birthDatePicker);

		JLabel creditCardIdLabel = new JLabel("Credit Card Id: ");
		creditCardIdInput = new InValuesInput(CreditCardQueries.getAllCreditCardId());
		LabelInputPanel creditCardIdPanel = new LabelInputPanel(creditCardIdLabel, creditCardIdInput);

		JLabel driverLicenseIdLabel = new JLabel("Driver LicenseId: ");
		driverLicenseIdInput = new InValuesInput(DriverLicenseQueries.getAllDriverLicenseId());
		LabelInputPanel driverLicenseIdPanel = new LabelInputPanel(driverLicenseIdLabel, driverLicenseIdInput);

		JLabel passwordLabel = new JLabel("Password: ");
		passwordInput = new NotInValuesInput(new ArrayList<String>());
		LabelInputPanel passwordPanel = new LabelInputPanel(passwordLabel, passwordInput);

		this.add(clientIdPanel);
		this.add(clientNamePanel);
		this.add(nationalityPanel);
		this.add(contactNumberPanel);
		this.add(expirationDatePanel);
		this.add(creditCardIdPanel);
		this.add(driverLicenseIdPanel);
		this.add(passwordPanel);
	}

	private void buildCreateNewClientButton() {
		searchButton = new JButton("New Client");
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
						String clientId = clientIdInput.getText();
						String clientName = clientNameInput.getText();
						String nationality = nationalityInput.getText();
						String contactNumber = contactNumberInput.getText();
						String creditCardId = creditCardIdInput.getText();
						String driverLicenseId = driverLicenseIdInput.getText();
						String password = passwordInput.getText();
						String roleAndType = "client";

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
						String dateString = formatter.format(birthDateModel.getValue());
						
						String[] clientInfo = {clientId, driverLicenseId, clientName, dateString, nationality, creditCardId, contactNumber};
						ClientQueries.addClient(clientInfo);
						
						String[] userInfo = {clientId, roleAndType, password, roleAndType};
						UserQueries.addUser(userInfo);



						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Client Created!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Client Created!",
								JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(NewClientWindowQuestions.this, "Error: " + ex.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(NewClientWindowQuestions.this, "Please enter the values",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !clientIdInput.getText().trim().isEmpty() && !clientNameInput.getText().trim().isEmpty()
				&& !nationalityInput.getText().trim().isEmpty() && !contactNumberInput.getText().trim().isEmpty()
				&& !creditCardIdInput.getText().trim().isEmpty() && !driverLicenseIdInput.getText().trim().isEmpty()
				&& !passwordInput.getText().trim().isEmpty() && birthDateModel.getValue() != null;
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

	private class InValuesInput extends ValidatingTrimmedTextField {
		public InValuesInput(List<String> validStrings) {
			super(validStrings);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void focusLost(FocusEvent e) {
			String text = getText().trim();
			if (text.isEmpty()) {
				return;
			}
			if (validStrings.contains(text)) {
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
