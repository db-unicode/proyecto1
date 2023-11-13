package uniandes.dpoo.proyecto1.userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LocalAdminInterface extends MainMenuInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		new LocalAdminInterface();
	}
	
	public LocalAdminInterface() {
		super();
		addNewEmployeeOption();
		addNewDriverLicenseOption();
		addNewCreditCardOption();
		addNewClientOption();
		addOpenCuadriculaCarrosButton();
	}
	
    private void addNewEmployeeOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new NewEmployeeWindow(LocalAdminInterface.this);
            }
        };
        this.addOption("New Employee", actionListener);
    }
    
    private void addNewDriverLicenseOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new NewDriverLicenseWindow(LocalAdminInterface.this);
            }
        };
        this.addOption("New Driver License", actionListener);
    }
    
    private void addNewCreditCardOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new NewCreditCardWindow(LocalAdminInterface.this);
            }
        };
        this.addOption("New Credit Card License", actionListener);
    }
    
    private void addNewClientOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new NewClientWindow(LocalAdminInterface.this);
            }
        };
        this.addOption("New Client", actionListener);
    }
}
