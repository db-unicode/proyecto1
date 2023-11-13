package uniandes.dpoo.proyecto1.userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DeliverCarReservationWindow extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel configPanel;
	private JPanel titleContainer;
	private JLabel titleLabel;
	private Frame owner;
	
	public static void main(String[] args) {
		new DeliverCarReservationWindow(null);
	}
	public DeliverCarReservationWindow(Frame owner) {
        super(owner, "Deliver Car Reservation", true);
        this.owner = owner;
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1024, 968);
        
        buildTitleContainer();
        buildConfigPanel();
        this.setVisible(true);
	}
	
	private void buildTitleContainer() {
		titleContainer = new JPanel();
		titleContainer.setPreferredSize(new Dimension(1024, 100));
		titleLabel = new JLabel("Deliver Car Reservation");
		Font titleFont = new Font("Arial", Font.ITALIC, 46);
		titleLabel.setFont(titleFont);
		titleContainer.add(titleLabel, BorderLayout.PAGE_START);
		this.add(titleContainer, BorderLayout.PAGE_START);
	}
	
	private void buildConfigPanel() {
		configPanel = new JPanel();
		configPanel.setPreferredSize(new Dimension(1024, 300));

		configPanel.add(new DeliverCarReservationWindowQuestions(owner, this));
		
		this.add(configPanel, BorderLayout.CENTER);
	}
}
