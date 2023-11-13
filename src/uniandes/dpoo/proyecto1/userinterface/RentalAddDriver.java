package uniandes.dpoo.proyecto1.userinterface;


import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uniandes.dpoo.proyecto1.queries.DriverLicenseQueries;
import uniandes.dpoo.proyecto1.queries.RentalDriversQueries;
import uniandes.dpoo.proyecto1.queries.RentalQueries;

public class RentalAddDriver extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String driverLicenseIdValue;
	private DriverLicenseIdInput driverLicenseIdInput;
	private String rentalId;
	
	public RentalAddDriver(Frame owner, String rentalId) {
		super(owner, "Add Driver", true);
		this.rentalId = rentalId;
        this.setLayout(new GridLayout(3,1));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 400);
        buildAddDriverPanel();
        this.setVisible(true);
	}
	
	private void buildAddDriverPanel() {
		JLabel driverLicenseId = new JLabel("Driver License Id: ");
		JLabel driverNameLabel = new JLabel("    ");
		JLabel driverNameHolder = new JLabel("Driver Name: ");
		driverLicenseIdInput = new DriverLicenseIdInput(DriverLicenseQueries.getAllDriverLicenseId(), driverNameLabel);
		LabelInputPanel addDriverPanel = new LabelInputPanel(driverLicenseId, driverLicenseIdInput);
		LabelInputPanel driverNamePanel = new LabelInputPanel(driverNameHolder, driverNameLabel);
		
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> confirmAction());
        
		this.add(addDriverPanel);
		this.add(driverNamePanel);
		this.add(confirmButton);

	}
	

	private void confirmAction() {
	    String inputValue = driverLicenseIdInput.getText().trim();
	    if (!inputValue.isEmpty()) {
	        driverLicenseIdValue = inputValue; 
	        try {
		        RentalDriversQueries.addDriver(rentalId, driverLicenseIdValue);
		        RentalQueries.sumDriverToTotalCost(rentalId);
		        String driverName = DriverLicenseQueries.getDriverName(driverLicenseIdValue);

		        JPanel notificationPanel = new JPanel();
		        notificationPanel.add(new JLabel("Driver '" + driverName + "' has been added."));

		        JOptionPane.showMessageDialog(this, notificationPanel, "Driver added", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Driver already added:" + e, "Error", JOptionPane.ERROR_MESSAGE);
			}


	        this.dispose(); 
	    } else {
	        JOptionPane.showMessageDialog(this, "Some fields are empty", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	
	private class DriverLicenseIdInput extends ValidatingTrimmedTextField {

		private JLabel outputLabel;

		public DriverLicenseIdInput(List<String> validStrings, JLabel outputLabel) {
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
				outputLabel.setText(DriverLicenseQueries.getDriverName(text));
			} else {
				setText("");
				outputLabel.setText("");
				JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}
}
