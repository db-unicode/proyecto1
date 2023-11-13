package uniandes.dpoo.proyecto1.userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CuadriculaCarros extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int FILAS = 7;
    private static final int COLUMNAS = 60;

    public CuadriculaCarros() {
        super("Cuadricula de Carros");
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel panelMeses = new JPanel(new GridLayout(1, COLUMNAS));
        String[] meses = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < meses.length; i++) {
            JLabel label = new JLabel(meses[i]);
            label.setHorizontalAlignment(JLabel.CENTER);
            panelMeses.add(label);
        }
        add(panelMeses, BorderLayout.NORTH);

        JPanel panelDias = new JPanel(new GridLayout(FILAS, 1));
        for (int i = 0; i < FILAS; i++) {
            JLabel label = new JLabel(obtenerNombreDia(i));
            label.setHorizontalAlignment(JLabel.CENTER);
            panelDias.add(label);
        }
        add(panelDias, BorderLayout.WEST);

        JPanel panelDiasNumeros = new JPanel(new GridLayout(FILAS, COLUMNAS));

        JButton[][] cuadricula = new JButton[FILAS][COLUMNAS];

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                int numeroCasilla = i + (j * FILAS);
                cuadricula[i][j] = new JButton(Integer.toString(numeroCasilla));
                cuadricula[i][j].addActionListener(new CuadriculaListener());
                panelDiasNumeros.add(cuadricula[i][j]);
            }
        }

        add(panelDiasNumeros, BorderLayout.CENTER);

        JPanel panelEscalaColores = new JPanel(new GridLayout(1, 5));

        Color[] colores = {Color.WHITE, new Color(173, 216, 230), new Color(135, 206, 250), new Color(0, 0, 205), new Color(0, 0, 128)};

        for (Color color : colores) {
            JPanel colorPanel = new JPanel();
            colorPanel.setBackground(color);
            panelEscalaColores.add(colorPanel);
        }

        add(panelEscalaColores, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setVisible(true);
    }

    private class CuadriculaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton boton = (JButton) e.getSource();
            int numeroCasilla = Integer.parseInt(boton.getText());

            Date fecha = obtenerFechaDesdeNumeroCasilla(numeroCasilla);
            int cantidadCarros = consultarCantidadCarrosEnFecha(fecha);

            boton.setBackground(getColorForCantidadCarros(cantidadCarros));

            System.out.println("En la fecha " + fecha + " hay " + cantidadCarros + " carros.");
        }

        private Color getColorForCantidadCarros(int cantidadCarros) {
            float scale = cantidadCarros / 100f;

            int red = (int) (173 + scale * (0 - 173));
            int green = (int) (216 + scale * (0 - 216));
            int blue = (int) (230 + scale * (205 - 230));

            return new Color(red, green, blue);
        }
    }

    private Date obtenerFechaDesdeNumeroCasilla(int numeroCasilla) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, numeroCasilla + 1);
        return calendar.getTime();
    }

    private int consultarCantidadCarrosEnFecha(Date fecha) {
        return (int) (Math.random() * 100);
    }

    private String obtenerNombreDia(int index) {
        String[] diasSemana = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        return diasSemana[index];
    }

    public static void main(String[] args) {
        new CuadriculaCarros();
    }
}