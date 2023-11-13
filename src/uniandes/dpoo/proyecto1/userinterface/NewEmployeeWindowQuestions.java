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


import uniandes.dpoo.proyecto1.queries.EmployeeQueries;
import uniandes.dpoo.proyecto1.queries.UserQueries;


public class NewEmployeeWindowQuestions extends JPanel {
	private Frame parentFrame;
	private static final long serialVersionUID = 1L;
	private EmployeeInput employeeIdInput;
	private EmployeeInput employeeNameInput;
	private EmployeeInput passwordInput;
	private JButton searchButton;
	private JDialog parentDialog;

	public NewEmployeeWindowQuestions(Frame parentFrame, JDialog parentDialog) {
		this.parentDialog = parentDialog;
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(1024, 300));
		this.setLayout(new GridLayout(3, 2));

		buildNewEmployeeInfo();
		buildCreateNewEmployeeButton();
	}

	private void buildNewEmployeeInfo() {
		JLabel employeeIdLabel = new JLabel("Employee Id: ");
		employeeIdInput = new EmployeeInput(EmployeeQueries.getAllEmpoyeeIds());
		LabelInputPanel employeeIdPanel = new LabelInputPanel(employeeIdLabel, employeeIdInput);

		JLabel employeeNameLabel = new JLabel("Employee Name: ");
		employeeNameInput = new EmployeeInput(new ArrayList<String>());
		LabelInputPanel employeeNamePanel = new LabelInputPanel(employeeNameLabel, employeeNameInput);

		JLabel passwordLabel = new JLabel("Employee Password");
		passwordInput = new EmployeeInput(new ArrayList<String>());
		LabelInputPanel passwordPanel = new LabelInputPanel(passwordLabel, passwordInput);
		
		this.add(employeeIdPanel);
		this.add(employeeNamePanel);
		this.add(passwordPanel);

	}

	private void buildCreateNewEmployeeButton() {
		searchButton = new JButton("Create New Employee");
		searchButton.addActionListener(getCreateNewEmployeeListener());
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchButtonPanel.add(searchButton);
		this.add(searchButtonPanel);
	}

	private ActionListener getCreateNewEmployeeListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFieldFilled()) {
					try {
						String employeeId = employeeIdInput.getText();
						String roleId = "1";
						String employeeName = employeeNameInput.getText();
						String locationId = UserSession.getEmployeeLocationId();
						
						String password = passwordInput.getText();
						String roleAndTypeUser = "employee";
						
						String[] employeeInfo = {employeeId, roleId, employeeName, locationId};
						EmployeeQueries.addEmployee(employeeInfo);
						
						String[] userInfo = {employeeId, roleAndTypeUser, password , roleAndTypeUser};
						UserQueries.addUser(userInfo);
						
						JPanel notificationPanel = new JPanel();
						notificationPanel.add(new JLabel("Employee Created!"));
						JOptionPane.showMessageDialog(parentFrame, notificationPanel, "Employee Created!", JOptionPane.INFORMATION_MESSAGE);
						parentDialog.dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(NewEmployeeWindowQuestions.this,
								"Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(NewEmployeeWindowQuestions.this,
							"Please enter the values", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

	private boolean isFieldFilled() {
		return !employeeIdInput.getText().trim().isEmpty() && !employeeNameInput.getText().trim().isEmpty() && !passwordInput.getText().trim().isEmpty();
	}

	private class EmployeeInput extends ValidatingTrimmedTextField {



		public EmployeeInput(List<String> validStrings) {
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
}
