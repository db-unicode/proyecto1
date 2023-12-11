package uniandes.dpoo.proyecto1.facturas;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FacturaImpresora {

    public static void generarFacturaPDF(String nombreCliente, String carCategory, String deliveryLocation, String monto) {
        UUID uuid = UUID.randomUUID();
        String nombreArchivo = "factura_" + uuid.toString() + ".pdf";

        Document document = new Document();

        try {
            // Obtener el directorio de trabajo actual del usuario
            String directorioActual = System.getProperty("user.dir");

            // Crear la carpeta "receipts" en el directorio de trabajo actual
            crearCarpetaSiNoExiste(directorioActual + "/receipts");

            // Generar la factura
            PdfWriter.getInstance(document, new FileOutputStream("receipts/" + nombreArchivo));
            document.open();

            FacturaFormateadora formateadora = new FacturaFormateadora();

            // Asegúrate de proporcionar la ruta correcta de la imagen de la firma
            String rutaImagen = directorioActual + "/data/img_firma-3-1200x550-cc.jpg";
            formateadora.formatoBasico(document, nombreCliente, carCategory, deliveryLocation, monto, rutaImagen);

            document.close();

            System.out.println("Factura generada exitosamente en: receipts/" + nombreArchivo);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void crearCarpetaSiNoExiste(String nombreCarpeta) {
        File carpeta = new File(nombreCarpeta);
        if (!carpeta.exists()) {
            if (carpeta.mkdir()) {
                System.out.println("Carpeta creada en: " + carpeta.getAbsolutePath());
            } else {
                System.out.println("Error al crear la carpeta: " + nombreCarpeta);
            }
        }
    }
}