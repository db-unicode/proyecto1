package uniandes.dpoo.proyecto1.userinterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.github.lgooddatepicker.components.TimePicker;

import uniandes.dpoo.proyecto1.queries.InsuranceQueries;
import uniandes.dpoo.proyecto1.rental.Rental;
import uniandes.dpoo.proyecto1.timetools.TimeRange;
import uniandes.dpoo.proyecto1.timetools.TimeUtil;

public class CarReservationSecondQuestionsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UtilDateModel deliveryDateModel;
	private UtilDateModel returnDateModel;
	private TimePicker deliveryStartHourPicker;
	private TimePicker returnStartHourPicker;
	private TimePicker deliveryEndHourPicker;
	private TimePicker returnEndHourPicker;
	private JDatePickerImpl deliveryDatePicker;
	private JDatePickerImpl returnDatePicker;
	private JDatePanelImpl deliveryDatePanel;
	private JDatePanelImpl returnDatePanel;
	private CheckBoxPanel insuranceCheckBoxes;
	private JButton reserveCarButton;
	private Rental currentRental;

	public CarReservationSecondQuestionsPanel() {
		this.setPreferredSize(new Dimension(1024, 500));
		this.setLayout(new GridLayout(4, 2));

		buildDatePickers();
		buildTimePickers();
		buildInsuranceCheckBoxes();
		buildReserveCarButton();
		this.enableInputs(false);
	}

	private void buildDatePickers() {
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");

		JLabel deliveryDateLabel = new JLabel("Delivery Date: ");
		deliveryDateModel = new UtilDateModel();
		deliveryDatePanel = new JDatePanelImpl(deliveryDateModel, p);
		deliveryDatePicker = new JDatePickerImpl(deliveryDatePanel, new DateLabelFormatter());
		LabelInputPanel deliveryDate = new LabelInputPanel(deliveryDateLabel, deliveryDatePicker);

		JLabel returnDateLabel = new JLabel("Return Date: ");
		returnDateModel = new UtilDateModel();
		returnDatePanel = new JDatePanelImpl(returnDateModel, p);
		returnDatePicker = new JDatePickerImpl(returnDatePanel, new DateLabelFormatter());
		LabelInputPanel returnDate = new LabelInputPanel(returnDateLabel, returnDatePicker);

		this.add(deliveryDate);
		this.add(returnDate);
	}

	private void buildTimePickers() {
		JLabel deliveryStartHourLabel = new JLabel("Delivery Start Hour: ");
		deliveryStartHourPicker = new TimePicker();
		LabelInputPanel deliveryStartHour = new LabelInputPanel(deliveryStartHourLabel, deliveryStartHourPicker);

		JLabel returnStartHourLabel = new JLabel("Return Start Hour: ");
		returnStartHourPicker = new TimePicker();
		LabelInputPanel returnStartHour = new LabelInputPanel(returnStartHourLabel, returnStartHourPicker);

		JLabel deliveryEndHourLabel = new JLabel("Delivery End Hour: ");
		deliveryEndHourPicker = new TimePicker();
		LabelInputPanel deliveryEndHour = new LabelInputPanel(deliveryEndHourLabel, deliveryEndHourPicker);

		JLabel returnEndHourLabel = new JLabel("Return End Hour: ");
		returnEndHourPicker = new TimePicker();
		LabelInputPanel returnEndHour = new LabelInputPanel(returnEndHourLabel, returnEndHourPicker);

		this.add(deliveryStartHour);
		this.add(returnStartHour);
		this.add(deliveryEndHour);
		this.add(returnEndHour);
	}

	private void buildInsuranceCheckBoxes() {
		insuranceCheckBoxes = new CheckBoxPanel(InsuranceQueries.getAllInsranceNames(), "Oferred Insurances");
		this.add(insuranceCheckBoxes);
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

	public class CheckBoxPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<JCheckBox> checkBoxes = new ArrayList<>();

		public CheckBoxPanel(List<String> options, String title) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(BorderFactory.createTitledBorder(title));
			for (String option : options) {
				JCheckBox checkBox = new JCheckBox(option);
				checkBoxes.add(checkBox);
				add(checkBox);
			}
		}

		public List<String> getSelectedCheckBoxes() {
			List<String> selected = new ArrayList<>();
			for (JCheckBox checkBox : checkBoxes) {
				if (checkBox.isSelected()) {
					selected.add(checkBox.getText());
				}
			}
			return selected;
		}

		public void setCheckboxesEnabled(boolean decision) {
			for (JCheckBox checkBox : checkBoxes) {
				checkBox.setEnabled(decision);
			}
		}

		public void clearCheckBoxes() {
			for (JCheckBox checkBox : checkBoxes) {
				checkBox.setSelected(false);
			}
		}
	}

	public void enableInputs(boolean decision) {
		deliveryDatePicker.getComponent(1).setEnabled(decision);
		deliveryDatePicker.getJFormattedTextField().setEnabled(decision);
		returnDatePicker.getComponent(1).setEnabled(decision);
		returnDatePicker.getJFormattedTextField().setEnabled(decision);

		deliveryDatePanel.setEnabled(decision);
		returnDatePanel.setEnabled(decision);
		deliveryStartHourPicker.setEnabled(decision);
		returnStartHourPicker.setEnabled(decision);
		deliveryEndHourPicker.setEnabled(decision);
		returnEndHourPicker.setEnabled(decision);
		deliveryDatePicker.setEnabled(decision);
		returnDatePicker.setEnabled(decision);
		deliveryDatePanel.setEnabled(decision);
		;
		returnDatePanel.setEnabled(decision);
		reserveCarButton.setEnabled(decision);
		insuranceCheckBoxes.setCheckboxesEnabled(decision);
	}

	private void buildReserveCarButton() {
		reserveCarButton = new JButton("Reserve Car");
		reserveCarButton.addActionListener(getReserveCarActionListener());
		JPanel reserveCarButtonPanel = new JPanel();
		reserveCarButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		reserveCarButtonPanel.add(reserveCarButton);
		this.add(reserveCarButtonPanel);
	}

	private ActionListener getReserveCarActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isAllFieldsFilledSecondQuestions()) {

					LocalDate deliveryLocalDate = deliveryDateModel.getValue().toInstant()
							.atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate returnLocalDate = returnDateModel.getValue().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();

					LocalTime deliveryStartHour = deliveryStartHourPicker.getTime();
					LocalTime returnStartHour = returnStartHourPicker.getTime();
					LocalTime deliveryEndHour = deliveryEndHourPicker.getTime();
					LocalTime returnEndHour = returnEndHourPicker.getTime();
					List<String> selectedInsurances = insuranceCheckBoxes.getSelectedCheckBoxes();
					boolean correctDatesAndTimes = !TimeUtil.isDateRangeValid(deliveryLocalDate, returnLocalDate)
							|| !TimeUtil.isTimeRangeValid(deliveryStartHour, returnStartHour)
							|| !TimeUtil.isTimeRangeValid(deliveryEndHour, returnEndHour);
					if (correctDatesAndTimes) {
						JOptionPane.showMessageDialog(CarReservationSecondQuestionsPanel.this, "Verify time ranges",
								"Error", JOptionPane.ERROR_MESSAGE);
						clearInputs();
						return;
					}
					try {
						currentRental.setDeliveryDayTime(deliveryLocalDate,
								new TimeRange<LocalTime>(deliveryStartHour, deliveryEndHour));
						currentRental.setReturnDayTime(returnLocalDate,
								new TimeRange<LocalTime>(returnStartHour, returnEndHour));
						currentRental.addInsuranceList(selectedInsurances);
						int costPerDay = currentRental.getCostPerDay();
						int totalDays = currentRental.getTotalDays();
						int totalCost = currentRental.getCalculatedTotalCost();

						// Prepare the message with cost details
						String message = "<html>Cost Per Day: " + costPerDay + "<br>Total Days: " + totalDays
								+ "<br>Total Cost: " + totalCost + "</html>";

						int option = JOptionPane.showConfirmDialog(CarReservationSecondQuestionsPanel.this, message,
								"Confirm Reservation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

						if (option == JOptionPane.YES_OPTION) {
							try {
								currentRental.payAPercentage(30);
								currentRental.saveRental();
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(CarReservationSecondQuestionsPanel.this,
										"Error: " + e2.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
								clearInputs();
							}
						} else {
							clearInputs();
							enableInputs(true);
						}
						enableInputs(false);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(CarReservationSecondQuestionsPanel.this,
								"Error: " + e2.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
						clearInputs();
					}

				} else {
					JOptionPane.showMessageDialog(CarReservationSecondQuestionsPanel.this, "Some fields are empty",
							"Error", JOptionPane.ERROR_MESSAGE);
					clearInputs();
				}
			}
		};
	}

	private boolean isAllFieldsFilledSecondQuestions() {
		return deliveryDateModel.getValue() != null && returnDateModel.getValue() != null
				&& deliveryStartHourPicker.getTime() != null && returnStartHourPicker.getTime() != null
				&& deliveryEndHourPicker.getTime() != null && returnEndHourPicker.getTime() != null;
	}

	public void setCurrentRental(Rental currentRental) {
		this.currentRental = currentRental;
	}

	public void clearInputs() {

		deliveryDateModel.setValue(null);
		returnDateModel.setValue(null);

		deliveryStartHourPicker.setTime(null);
		returnStartHourPicker.setTime(null);
		deliveryEndHourPicker.setTime(null);
		returnEndHourPicker.setTime(null);

		insuranceCheckBoxes.clearCheckBoxes();
	}

}
