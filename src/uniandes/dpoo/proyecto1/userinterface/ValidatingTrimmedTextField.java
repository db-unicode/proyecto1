package uniandes.dpoo.proyecto1.userinterface;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class ValidatingTrimmedTextField extends JTextField implements FocusListener {

    private static final long serialVersionUID = 1L;
    protected List<String> validStrings;

    
    public ValidatingTrimmedTextField(List<String> validStrings) {
        this.validStrings = validStrings;
        this.addFocusListener(this);
        this.setPreferredSize(new Dimension(200, 30)); 
    }

    @Override
    public void focusGained(FocusEvent e) {
        // No es necesario hacer nada cuando se gana el foco
    }

    @Override
    public void focusLost(FocusEvent e) {
        String text = getText().trim();
        if (text.isEmpty()) {
            return;
        }
        if (!validStrings.contains(text)) {
        	setText("");
            JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String getText() {
        return super.getText().trim();
    }
}

