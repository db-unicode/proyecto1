package uniandes.dpoo.proyecto1.userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class EmployeeInterface extends MainMenuInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		new EmployeeInterface();
	}
	
	public EmployeeInterface() {
		super();
		addDeliverCarOption();
		addReceiveCarOption();
		addChangeCarStatusOption();
	}
	
    private void addDeliverCarOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new DeliverCarReservationWindow(EmployeeInterface.this);

            }
        };
        this.addOption("Deliver Car", actionListener);
    }
    
    private void addReceiveCarOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new ReceiveCarWindow(EmployeeInterface.this);

            }
        };
        this.addOption("Receieve Car", actionListener);
    }
    
    private void addChangeCarStatusOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new ChangeCarStatusWindow(EmployeeInterface.this);

            }
        };
        this.addOption("Change Car Status", actionListener);
    }
}
