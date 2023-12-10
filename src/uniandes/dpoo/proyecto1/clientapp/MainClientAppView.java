package uniandes.dpoo.proyecto1.clientapp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uniandes.dpoo.proyecto1.userinterface.LoginWindow;
import uniandes.dpoo.proyecto1.userinterface.MainMenuInterface;
import uniandes.dpoo.proyecto1.userinterface.NewClientWindow;

public class MainClientAppView extends MainMenuInterface {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		new MainClientAppView();
	}
	
	public MainClientAppView() {
		super();
		addNewClientOption();
		addLoginOption();
	}
	
    private void addNewClientOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new NewClientWindow(MainClientAppView.this);
            }
        };
        this.addOption("Register New Client", actionListener);
    }
    
    private void addLoginOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new LoginWindow();
              dispose();
            }
        };
        this.addOption("Client Login", actionListener);
    }
}
