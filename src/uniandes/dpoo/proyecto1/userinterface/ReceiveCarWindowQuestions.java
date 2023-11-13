package uniandes.dpoo.proyecto1.userinterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uniandes.dpoo.proyecto1.queries.CarQueries;
import uniandes.dpoo.proyecto1.queries.CarStatusQueries;
import uniandes.dpoo.proyecto1.queries.RentalQueries;
import uniandes.dpoo.proyecto1.queries.RentalStatusQueries;


public class ReceiveCarWindowQuestions extends JPanel {
	/**
	 * 
	 */
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;
	private RentalIdInput rentalIdInput;
	private JButton searchButton;
	private JDialog parentDialog;

	public ReceiveCarWindowQuestions(Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(2, 2));

		buildReservationId();
		buildReceiveCarButton();
	}

	private void buildReservationId() {
		JLabel reservationInfoLabel = new JLabel("Rental Info: ");
		JLabel rentalActualInfoLabel = new JLabel("‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎           ");
		LabelInputPanel rentalInfoPanel = new LabelInputPanel(reservationInfoLabel, rentalActualInfoLabel);

		JLabel carInfoLabel = new JLabel("Car Info: ");
		JLabel carActualInfoLabel = new JLabel(" ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ");
		LabelInputPanel carInfoPanel = new LabelInputPanel(carInfoLabel, carActualInfoLabel);

		JLabel reservationIdLabel = new JLabel("Rental ID: ");
		try {
			rentalIdInput = new RentalIdInput(RentalQueries.getAllRentalOfLocationIdWithStatus(UserSession.getEmployeeLocationId(), RentalStatusQueries.getRentalStatusId("rented")),
					rentalActualInfoLabel, carActualInfoLabel);
		} catch (Exception e) {
			rentalIdInput = new RentalIdInput(new ArrayList<String>(),
					rentalActualInfoLabel, carActualInfoLabel);
		}

		LabelInputPanel rentalPanel = new LabelInputPanel(reservationIdLabel, rentalIdInput);

		this.add(rentalPanel);
		this.add(carInfoPanel);
		this.add(rentalInfoPanel);

	}



	private void buildReceiveCarButton() {
		searchButton = new JButton("Receive Car");
		searchButton.addActionListener(getReceiveCarActionListener());
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchButtonPanel.add(searchButton);
		this.add(searchButtonPanel);
	}

	private ActionListener getReceiveCarActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFieldFilled()) {
					String reservationId = rentalIdInput.getText();
					try {
						String carId = RentalQueries.getCarId(reservationId);

						CarQueries.changeCarStatusTo(carId, CarStatusQueries.getCarStatusId("available"));
						RentalQueries.changeRentalStatusTo(reservationId,
								RentalStatusQueries.getRentalStatusId("returned"));
						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Returned!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Returned", JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(ReceiveCarWindowQuestions.this,
								"Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(ReceiveCarWindowQuestions.this,
							"Please enter a reservation ID", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !rentalIdInput.getText().trim().isEmpty();
	}

	private class RentalIdInput extends ValidatingTrimmedTextField {

		private JLabel rentalInfoOutput;
		private JLabel carInfoOutput;

		public RentalIdInput(List<String> validStrings, JLabel rentalInfoOutput, JLabel carInfoOutput) {
			super(validStrings);
			this.rentalInfoOutput = rentalInfoOutput;
			this.carInfoOutput = carInfoOutput;
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void focusLost(FocusEvent e) {
			String text = getText().trim();
			if (text.isEmpty()) {
				return;
			}
			if (validStrings.contains(text)) {
				rentalInfoOutput.setText(RentalQueries.getRentalInfo(text));
				carInfoOutput.setText(CarQueries.getCarInfo(RentalQueries.getCarId(text)));
			} else {
				setText("");
				carInfoOutput.setText("");
				rentalInfoOutput.setText("");
				JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
