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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uniandes.dpoo.proyecto1.queries.CarQueries;
import uniandes.dpoo.proyecto1.queries.CarStatusQueries;


public class ChangeCarStatusWindowQuestions extends JPanel {
	/**
	 * 
	 */
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;
	private CarIdInput carIdInput;
	private JButton searchButton;
	private JDialog parentDialog;
	private JComboBox<String> statusSelector;

	public ChangeCarStatusWindowQuestions(Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(2, 2));

		buildCarId();
		buildStatusSelector();
		buildChangeCarStatusButton();
	}

	private void buildCarId() {


		JLabel carInfoLabel = new JLabel("Car Info: ");
		JLabel carActualInfoLabel = new JLabel(" ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ");
		LabelInputPanel carInfoPanel = new LabelInputPanel(carInfoLabel, carActualInfoLabel);

		JLabel carIDLabel = new JLabel("Car ID: ");

		carIdInput = new CarIdInput(CarQueries.getAllCarIdOfALocationId(UserSession.getEmployeeLocationId()),
				carActualInfoLabel);
		LabelInputPanel carIdPanel = new LabelInputPanel(carIDLabel, carIdInput);

		this.add(carIdPanel);
		this.add(carInfoPanel);

	}

	private void buildStatusSelector() {
		statusSelector = new JComboBox<String>();
		for (String status: CarStatusQueries.getAllStatusName()) {
			statusSelector.addItem(status);
		}
		JLabel statusSelectorLabel = new JLabel("Select New Car Status");
		LabelInputPanel statusSelectorPanel = new LabelInputPanel(statusSelectorLabel, statusSelector);
		
		this.add(statusSelectorPanel);
	}

	private void buildChangeCarStatusButton() {
		searchButton = new JButton("Change Car Status");
		searchButton.addActionListener(getChangeCarStatusActionListener());
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchButtonPanel.add(searchButton);
		this.add(searchButtonPanel);
	}

	private ActionListener getChangeCarStatusActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFieldFilled()) {
					String carId = carIdInput.getText();
					String newStatus = (String) statusSelector.getSelectedItem();
					try {
						CarQueries.changeCarStatusTo(carId, CarStatusQueries.getCarStatusId(newStatus));
						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Car Status Changed!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Car Status Changed", JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(ChangeCarStatusWindowQuestions.this,
								"Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(ChangeCarStatusWindowQuestions.this,
							"Please enter a car ID", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !carIdInput.getText().trim().isEmpty();
	}

	private class CarIdInput extends ValidatingTrimmedTextField {


		private JLabel carInfoOutput;

		public CarIdInput(List<String> validStrings, JLabel carInfoOutput) {
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
