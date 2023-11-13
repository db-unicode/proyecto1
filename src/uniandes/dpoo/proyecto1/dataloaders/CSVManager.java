package uniandes.dpoo.proyecto1.dataloaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;

import joinery.DataFrame;

public class CSVManager {

    private CSVManager() {
    }

    public static DataFrame<Object> loadCSV(String csvFilePath) throws IOException {
        checkCSVFilePath(csvFilePath);
        String content = new String(Files.readAllBytes(Paths.get(csvFilePath)));
        content = content.replace('-', '_').replace('/', '_'); // correción formato fechas
        Files.write(Paths.get(csvFilePath), content.getBytes());
        DataFrame<Object> df = DataFrame.readCsv(csvFilePath).convert(String.class);
        return df;
    }


    public static ArrayList<String> getColumnNames(String csvFilePath) throws IOException {
        checkCSVFilePath(csvFilePath);
        DataFrame<Object> df = DataFrame.readCsv(csvFilePath);
        Set<Object> columnSet = df.columns();
        ArrayList<String> columnNames = new ArrayList<>();
        for (Object column : columnSet) {
            columnNames.add(column.toString());
        }
        return columnNames;
    }
    
    private static void checkCSVFilePath(String csvFilePath) {
        if (csvFilePath == null || csvFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV file path cannot be null or empty.");
        }
    }
}

