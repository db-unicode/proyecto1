package uniandes.dpoo.proyecto1.userinterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uniandes.dpoo.proyecto1.queries.CarCategoryQueries;
import uniandes.dpoo.proyecto1.queries.ClientQueries;
import uniandes.dpoo.proyecto1.queries.LocationQueries;
import uniandes.dpoo.proyecto1.rental.Rental;



public class CarReservationInitQuestionsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> carCategoryPicker;
	private JComboBox<String> locationNamePicker;
	private CarReservationSecondQuestionsPanel carReservationSecondQuestionsPanel;
	private ClientIdInput clientIdInput;
	private JButton searchCarButton;
	private Rental currentRental;
	
	public CarReservationInitQuestionsPanel() {
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(3, 2));

		buildClientIdAndName();
		buildCarCategory();
		buildDeliveryLocation();
		buildSearchCarButton();

	}
	
	private void buildSearchCarButton() {
		searchCarButton = new JButton("Search Car");
		searchCarButton.addActionListener(getSearchCarActionListener());
		JPanel searchCarButtonPanel = new JPanel();
		searchCarButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchCarButtonPanel.add(searchCarButton);
		this.add(searchCarButtonPanel);
	}

	private ActionListener getSearchCarActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isAllFieldsFilledInitQuestions()) {
					String clientId = clientIdInput.getText();
					String carCategory = (String) carCategoryPicker.getSelectedItem();
					String deliveryLocation = (String) locationNamePicker.getSelectedItem();
					try {
						currentRental = new Rental(clientId, carCategory, deliveryLocation);

						clientIdInput.setEnabled(false);
						carCategoryPicker.setEnabled(false);
						locationNamePicker.setEnabled(false);
						searchCarButton.setEnabled(false);
						carReservationSecondQuestionsPanel.enableInputs(true);
						carReservationSecondQuestionsPanel.setCurrentRental(currentRental);
						checkChangedDesiredLocation();
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(CarReservationInitQuestionsPanel.this,
								"Not Cars Found, try a different category", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(CarReservationInitQuestionsPanel.this, "Some fields are empty", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}
	
	private boolean isAllFieldsFilledInitQuestions() {
		return !clientIdInput.getText().trim().isEmpty() && carCategoryPicker.getSelectedItem() != null
				&& locationNamePicker.getSelectedItem() != null;
	}
	
	private void checkChangedDesiredLocation() {
	    if (currentRental.isChangedDesiredLocation()) {
	        String msg = "Your perfect car is in another location, the new delivery location is "
	                + currentRental.getDeliveryLocation()
	                + "\n Do you want us to move the car to your desired location (this costs 30$ extra)?";
	        
	        // Create custom dialog
	        JDialog dialog = new JDialog();
	        dialog.setModal(true); // Make dialog modal
	        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Prevent closing with 'X' button
	        dialog.setLayout(new FlowLayout());

	        // Add message label
	        JLabel messageLabel = new JLabel(msg);
	        dialog.add(messageLabel);

	        // Add Yes and No buttons
	        JButton yesButton = new JButton("Yes");
	        yesButton.addActionListener(e -> {
	            currentRental.acceptRelocationToDesiredLocation();
	            dialog.dispose();
	        });
	        dialog.add(yesButton);

	        JButton noButton = new JButton("No");
	        noButton.addActionListener(e -> {
	            dialog.dispose();
	        });
	        dialog.add(noButton);

	        // Set dialog size and make it visible
	        dialog.pack();
	        dialog.setLocationRelativeTo(this); // Set location relative to parent component
	        dialog.setVisible(true);
	    } else {
	        // Display a dialog with the message "Car Found!"
	        JOptionPane.showMessageDialog(this, "Car Found!", "Information", JOptionPane.INFORMATION_MESSAGE);
	    }
	}


	


	private void buildCarCategory() {
		JLabel carCategoryLabel = new JLabel("Car Category: ");
		carCategoryPicker = new JComboBox<>(new Vector<>(CarCategoryQueries.getAllCarCategorys()));
		LabelInputPanel carCategoryPanel = new LabelInputPanel(carCategoryLabel, carCategoryPicker);
		this.add(carCategoryPanel);
	}

	private void buildDeliveryLocation() {
		JLabel deliveryLocationLabel = new JLabel("Delivery Location: ");
		locationNamePicker = new JComboBox<>(new Vector<>(LocationQueries.getAllLocationNames()));
		LabelInputPanel locationPanel = new LabelInputPanel(deliveryLocationLabel, locationNamePicker);
		this.add(locationPanel);
	}

	private void buildClientIdAndName() {
		JLabel clientNameLabel = new JLabel("Client Name: ");
		JLabel clientCurrentNameLabel = new JLabel("‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ");
		LabelInputPanel clientNamePanel = new LabelInputPanel(clientNameLabel, clientCurrentNameLabel);

		JLabel clientIdLabel = new JLabel("Client ID: ");
		clientIdInput = new ClientIdInput(ClientQueries.getAllClientId(), clientCurrentNameLabel);
		LabelInputPanel clientIdPanel = new LabelInputPanel(clientIdLabel, clientIdInput);

		this.add(clientIdPanel);
		this.add(clientNamePanel);
	}

	
	
	
	private class ClientIdInput extends ValidatingTrimmedTextField {

		private JLabel outputLabel;

		public ClientIdInput(List<String> validStrings, JLabel outputLabel) {
			super(validStrings);
			this.outputLabel = outputLabel;
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void focusLost(FocusEvent e) {
			String text = getText().trim();
			if (text.isEmpty()) {
				return;
			}
			if (validStrings.contains(text)) {
				outputLabel.setText(ClientQueries.getClientName(text));
			} else {
				setText("");
				outputLabel.setText("");
				JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}




	public CarReservationSecondQuestionsPanel getCarReservationSecondQuestionsPanel() {
		return carReservationSecondQuestionsPanel;
	}

	public void setCarReservationSecondQuestionsPanel(
			CarReservationSecondQuestionsPanel carReservationSecondQuestionsPanel) {
		this.carReservationSecondQuestionsPanel = carReservationSecondQuestionsPanel;
	}
}
