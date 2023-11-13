package uniandes.dpoo.proyecto1.userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class GeneralAdminInterface extends MainMenuInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		new GeneralAdminInterface();
	}
	
	public GeneralAdminInterface() {
		super();
		addNewCarOption();
		deleteCarOption();
	}
	
    private void addNewCarOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new AddCarWindow(GeneralAdminInterface.this);
            }
        };
        this.addOption("New Car", actionListener);
    }
    
    private void deleteCarOption() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new DeleteCarWindow(GeneralAdminInterface.this);
            }
        };
        this.addOption("Delete Car", actionListener);
    }
}
