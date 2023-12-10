package uniandes.dpoo.proyecto1.userinterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientInterface extends MainMenuInterface {

	
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new ClientInterface();
    }

    public ClientInterface() {
        super();
        addReserveCarOption();
        addCheckAvailabilityOption();
    }

    private void addReserveCarOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog carReservationWindow = new CarReservationWindow(ClientInterface.this);
                carReservationWindow.setVisible(true);
            }
        };
        this.addOption("Reserve a Car", actionListener);
    }
    
    private void addCheckAvailabilityOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog carReservationWindow = new ValidateAvailabilityWindow(ClientInterface.this);
                carReservationWindow.setVisible(true);
            }
        };
        this.addOption("Check Availability", actionListener);
    }
}

