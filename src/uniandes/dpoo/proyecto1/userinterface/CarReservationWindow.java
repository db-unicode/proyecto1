package uniandes.dpoo.proyecto1.userinterface;

import javax.swing.*;



import java.awt.*;


public class CarReservationWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel configPanel;

	private JPanel titleContainer;
	private JLabel titleLabel;
	


	public static void main(String[] args) {
		new CarReservationWindow(null);
	}

    public CarReservationWindow(Frame owner) {
        super(owner, "Car Reservation", true);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1024, 968);

        buildTitleContainer();
        buildConfigPanel();
    }

	private void buildConfigPanel() {
		configPanel = new JPanel();
		configPanel.setPreferredSize(new Dimension(1024, 300));
		CarReservationInitQuestionsPanel carReservationInitQuestionsPanel = new CarReservationInitQuestionsPanel();
		CarReservationSecondQuestionsPanel carReservationSecondQuestionsPanel = new CarReservationSecondQuestionsPanel();
		
		carReservationInitQuestionsPanel.setCarReservationSecondQuestionsPanel(carReservationSecondQuestionsPanel);
		configPanel.add(carReservationInitQuestionsPanel);
		configPanel.add(carReservationSecondQuestionsPanel);
		
		this.add(configPanel, BorderLayout.CENTER);
	}

	private void buildTitleContainer() {
		titleContainer = new JPanel();
		titleContainer.setPreferredSize(new Dimension(1024, 100));
		titleLabel = new JLabel("Car Reservation");
		Font titleFont = new Font("Arial", Font.ITALIC, 46);
		titleLabel.setFont(titleFont);
		titleContainer.add(titleLabel, BorderLayout.PAGE_START);
		this.add(titleContainer, BorderLayout.PAGE_START);
	}

	

}
