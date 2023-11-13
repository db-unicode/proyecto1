package uniandes.dpoo.proyecto1.userinterface;

import javax.swing.*;

import uniandes.dpoo.proyecto1.queries.UserQueries;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField userIdField;
	private JPasswordField passwordField;

	public LoginWindow() {
		setTitle("RentACar Login");
		setSize(200, 300);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));

		add(createTitlePanel(), BorderLayout.NORTH);
		add(createLoginForm(), BorderLayout.CENTER);
		add(createSubmitButton(), BorderLayout.SOUTH);

		setVisible(true);
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout());
		JLabel titleLabel = new JLabel("RentACar Login");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		titlePanel.add(titleLabel);
		return titlePanel;
	}

	private JPanel createLoginForm() {
		JPanel loginForm = new JPanel();
		loginForm.setLayout(new GridLayout(4, 1, 5, 5));

		loginForm.add(new JLabel("User ID"));
		userIdField = new JTextField();
		userIdField.setPreferredSize(new Dimension(200, 40));
		loginForm.add(userIdField);

		loginForm.add(new JLabel("Password"));
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(200, 40));
		passwordField.setEchoChar('*');
		loginForm.add(passwordField);

		return loginForm;
	}

	private JButton createSubmitButton() {
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performLogin();
			}
		});
		return submitButton;
	}

	private void performLogin() {
		String userId = userIdField.getText();
		char[] password = passwordField.getPassword();
		String passwordString = new String(password);

		if (validateCredentials(userId, passwordString)) {
			JOptionPane.showMessageDialog(this, "Login Successful!");
			setUserSession(userId, passwordString);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Wrong credentials", "Error", JOptionPane.ERROR_MESSAGE);
			userIdField.setText("");
			passwordField.setText("");
		}
	}

	private void setUserSession(String userId, String password) {
		UserInterfaceFactory.launchUserInterface(userId, password);
	}
	
	private boolean validateCredentials(String userId, String password) {
		return UserQueries.validateUserCredentials(userId, password);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(LoginWindow::new);
	}
}
