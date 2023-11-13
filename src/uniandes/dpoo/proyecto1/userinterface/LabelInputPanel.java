package uniandes.dpoo.proyecto1.userinterface;

import javax.swing.*;
import java.awt.*;

public class LabelInputPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label;
    private JComponent inputComponent;

    public LabelInputPanel(JLabel label, JComponent inputComponent) {
        this.label = label;
        this.inputComponent = inputComponent;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(this.label);
        add(this.inputComponent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Label Input Panel Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 100);
            JLabel label = new JLabel("Nombre:");
            JTextField textField = new JTextField(20); 
            LabelInputPanel labelInputPanel = new LabelInputPanel(label, textField);
            frame.add(labelInputPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
