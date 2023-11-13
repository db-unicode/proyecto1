package uniandes.dpoo.proyecto1.userinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuInterface extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel optionsContainer;
    private JPanel mainTitleContainer;
    private JLabel mainTitleLabel;
    
    public static void main(String[] args) {
	    new MainMenuInterface();
	}

	public MainMenuInterface() {
        this.setTitle("RentACar");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1366, 768);
        
        buildMainTitle();
        buildSpacers();
        buildOptionsContainer();
        this.setVisible(true);
    }
    
    protected void addOption(String optionName, ActionListener actionListener) {
        optionsContainer.add(new OptionButton(optionName, actionListener));
        this.setVisible(true);
    }
    
	private void buildMainTitle() {
        mainTitleContainer = new JPanel();
        mainTitleContainer.setPreferredSize(new Dimension(1366,150));
        this.add(mainTitleContainer, BorderLayout.NORTH);
        
        mainTitleLabel = new JLabel("RentACar");
        Font mainTitleFont = new Font("Arial", Font.ITALIC, 76);
        mainTitleLabel.setFont(mainTitleFont);
        mainTitleContainer.add(mainTitleLabel);
    }
    
    private void buildOptionsContainer() {
        optionsContainer = new JPanel();
        optionsContainer.setLayout(new GridLayout(0, 1, 10, 20));

        this.add(optionsContainer);
    }
    
    private void buildSpacers() {
        JPanel lineStart = new JPanel();
        JPanel lineEnd = new JPanel();
        JPanel endPage = new JPanel();
        
        lineStart.setPreferredSize(new Dimension(400, 1000));
        lineEnd.setPreferredSize(new Dimension(400, 1000));
        endPage.setPreferredSize(new Dimension(1366, 150));
        
        this.add(lineStart, BorderLayout.LINE_START);
        this.add(lineEnd, BorderLayout.LINE_END);
        this.add(endPage, BorderLayout.PAGE_END);
    } 
    
    protected void addOpenCuadriculaCarrosButton() {
        JButton button = new JButton("Open CuadriculaCarros");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCuadriculaCarros();
            }
        };
        button.addActionListener(actionListener);
        addOption("Open CuadriculaCarros", actionListener);
    }

    private void openCuadriculaCarros() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Create an instance of CuadriculaCarros
                    CuadriculaCarros cuadriculaCarros = new CuadriculaCarros();
                    cuadriculaCarros.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private class OptionButton extends JButton {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
        public OptionButton(String text, ActionListener actionListener) {
            super(text);
            this.addActionListener(actionListener);

            Font buttonFont = new Font("Arial", Font.BOLD, 25);
            this.setFont(buttonFont);
        }
	}
}
