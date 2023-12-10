package uniandes.dpoo.proyecto1.dataloaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.ArrayList;
import joinery.DataFrame;

public class CSVManagerTest {

    @TempDir
    Path tempDir;

    String testCSVPath;

    @BeforeEach
    void setUp() throws IOException {
        testCSVPath = tempDir.resolve("test.csv").toString();
        String csvContent = "car_id,status_id,location_id,category_id,brand,model,plate,color,type\n" +
                            "C0001,2,L001,0,Toyota,Corolla,XYZ123,Red,car\n" +
                            "C0002,0,L002,1,Honda,Civic,ABC456,Blue,car\n";
        Files.write(tempDir.resolve("test.csv"), csvContent.getBytes());
    }

    @Test
    void testLoadCSV() throws IOException {
        DataFrame<Object> df = CSVManager.loadCSV(testCSVPath);
        Assertions.assertNotNull(df);
        Assertions.assertEquals(2, df.length()); 
    }

    @Test
    void testLoadCSVWithInvalidPath() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CSVManager.loadCSV(""); 
        });
    }

    @Test
    void testGetColumnNames() throws IOException {
        ArrayList<String> columnNames = CSVManager.getColumnNames(testCSVPath);
        Assertions.assertNotNull(columnNames);
        Assertions.assertEquals(9, columnNames.size()); 
        Assertions.assertEquals("car_id", columnNames.get(0)); 
    }

    @Test
    void testGetColumnNamesWithInvalidPath() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CSVManager.getColumnNames(""); 
        });
    }
}

