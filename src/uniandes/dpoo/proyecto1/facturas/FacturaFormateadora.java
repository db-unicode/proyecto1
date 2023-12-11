package uniandes.dpoo.proyecto1.facturas;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.Image;


import java.io.IOException;

public class FacturaFormateadora {

    public void formatoBasico(Document document, String nombreCliente, String descripcionVehiculo, String nombreSede, String monto, String rutaFirma) throws DocumentException, IOException {
        // Contenido de la factura
        document.add(new Paragraph("Factura"));
        document.add(new Paragraph("--------------------------"));
        document.add(new Paragraph("Cliente: " + nombreCliente));
        document.add(new Paragraph("Descripción del vehículo: " + descripcionVehiculo));
        document.add(new Paragraph("Nombre sede: " + nombreSede));
        document.add(new Paragraph("Monto: $" + monto));
        document.add(new Paragraph("--------------------------"));

        // Insertar la firma del administrador como imagen
        Image imagenFirma = Image.getInstance(rutaFirma);
        imagenFirma.scaleToFit(150, 50); // Ajusta el tamaño de la imagen
        document.add(imagenFirma);
    }

}