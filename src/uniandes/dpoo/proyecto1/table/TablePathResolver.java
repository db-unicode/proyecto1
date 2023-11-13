package uniandes.dpoo.proyecto1.table;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TablePathResolver {

	public static String getCSVFilePath(String directoryPath) {
		String pathFixString = "\\" + TablePathResolver.getTableName(directoryPath) + ".csv";
		return directoryPath.concat(pathFixString);
	}

	public static String getJSONFilePath(String directoryPath) {
		String pathFixString = "\\" + TablePathResolver.getTableName(directoryPath) + "_metadata.json";
		return directoryPath.concat(pathFixString);
	}
	
	public static String getTableName(String directoryPath) {
        Path path = Paths.get(directoryPath);
        String tableName = path.getFileName().toString(); 
		return tableName;
	}
}
