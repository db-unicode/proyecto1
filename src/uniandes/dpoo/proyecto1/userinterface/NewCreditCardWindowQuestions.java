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

import uniandes.dpoo.proyecto1.queries.CreditCardQueries;



public class NewCreditCardWindowQuestions extends JPanel{
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;
	private CreditCardInput creditCardIdInput;
	private CreditCardInput cvcInput;
	private UtilDateModel expirationDateModel;
	private JDatePickerImpl expirationDatePicker;
	private JDatePanelImpl expirationDatePanel;
	private JButton searchButton;
	private JDialog parentDialog;

	public NewCreditCardWindowQuestions (Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(3, 2));

		buildCreditCardInfo();
		buildCreateNewCreditCardLicenseButton();
	}

	private void buildCreditCardInfo() {
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		JLabel creditCardIdLabel = new JLabel("Credit Card Id: ");
		creditCardIdInput = new CreditCardInput(CreditCardQueries.getAllCreditCardId());
		LabelInputPanel creditCardIdPanel = new LabelInputPanel(creditCardIdLabel, creditCardIdInput);

		JLabel cvcLabel = new JLabel("CVC: ");
		cvcInput = new CreditCardInput(new ArrayList<String>());
		LabelInputPanel cvcPanel = new LabelInputPanel(cvcLabel, cvcInput);

		JLabel expirationDateLabel = new JLabel("Expiration Date: ");
		expirationDateModel = new UtilDateModel();
		expirationDatePanel = new JDatePanelImpl(expirationDateModel, p);
		expirationDatePicker = new JDatePickerImpl(expirationDatePanel, new DateLabelFormatter());
		LabelInputPanel expirationDatePanel = new LabelInputPanel(expirationDateLabel, expirationDatePicker);
		
		this.add(creditCardIdPanel);
		this.add(cvcPanel);
		this.add(expirationDatePanel);
	}

	private void buildCreateNewCreditCardLicenseButton() {
		searchButton = new JButton("New Credit Card");
		searchButton.addActionListener(getCreateNewCreditCardListener());
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchButtonPanel.add(searchButton);
		this.add(searchButtonPanel);
	}

	private ActionListener getCreateNewCreditCardListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFieldFilled()) {
					try {
						String creditCardId = creditCardIdInput.getText();
						String cvc = cvcInput.getText();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
						String dateString = formatter.format(expirationDateModel.getValue());
						
						String[] creditCardInfo = {creditCardId , cvc, dateString, "false"};
						CreditCardQueries.addCreditCard(creditCardInfo);
		
						
						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Credit Card Created!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Credit Card Created!", JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(NewCreditCardWindowQuestions .this,
								"Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(NewCreditCardWindowQuestions.this,
							"Please enter the values", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !creditCardIdInput.getText().trim().isEmpty() && !cvcInput.getText().trim().isEmpty() && expirationDateModel.getValue() != null;
	}

	private class CreditCardInput extends ValidatingTrimmedTextField {
		public CreditCardInput(List<String> validStrings) {
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
