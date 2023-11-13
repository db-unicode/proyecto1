package uniandes.dpoo.proyecto1.userinterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uniandes.dpoo.proyecto1.queries.CarQueries;

public class DeleteCarWindowQuestions extends JPanel {
	/**
	 * 
	 */
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;
	private carIdInput carIdInput;
	private JButton searchButton;
	private JDialog parentDialog;

	public DeleteCarWindowQuestions(Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(3, 1));

		buildCarId();
		buildDeleteCarButton();
	}

	private void buildCarId() {
		JLabel carIdLabel = new JLabel("Car ID: ");
		JLabel carInfoLabel = new JLabel("Car Info: ");
		JLabel carActualInfoLabel = new JLabel("");
		carIdInput = new carIdInput(CarQueries.getAllCarId(), carActualInfoLabel);

		LabelInputPanel carIdPanel = new LabelInputPanel(carIdLabel, carIdInput);
		LabelInputPanel carInfoPanel = new LabelInputPanel(carInfoLabel, carActualInfoLabel);

		this.add(carIdPanel);
		this.add(carInfoPanel);
	}

	private void buildDeleteCarButton() {
		searchButton = new JButton("Delete Car");
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
					try {
						String carId = carIdInput.getText();
						CarQueries.deleteCar(carId);

						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Car Deleted!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Car Deleted!",
								JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(DeleteCarWindowQuestions.this, "Error: " + ex.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(DeleteCarWindowQuestions.this, "Please enter a reservation ID",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !carIdInput.getText().trim().isEmpty();
	}

	private class carIdInput extends ValidatingTrimmedTextField {

		private JLabel carInfoOutput;

		public carIdInput(List<String> validStrings, JLabel carInfoOutput) {
			super(validStrings);
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
				carInfoOutput.setText(CarQueries.getCarInfo(text));
			} else {
				setText("");
				carInfoOutput.setText("");
				JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
