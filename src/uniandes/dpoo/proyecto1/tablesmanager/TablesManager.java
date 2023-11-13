package uniandes.dpoo.proyecto1.tablesmanager;

import java.io.File;
import uniandes.dpoo.proyecto1.table.Table;
import java.util.LinkedHashMap;


public class TablesManager {
	private static LinkedHashMap<String, Table> tables = new LinkedHashMap<String, Table>();
	private static final String DATADIRECTORYPATH = "data";
	private static final File DATADIRECTORY = new File(DATADIRECTORYPATH);
	
	public static boolean loadTables() throws Exception {
		File[] tableDirectorys = TablesManager.DATADIRECTORY.listFiles(File::isDirectory);
		for (File tableDirectory : tableDirectorys) {
			String tableDirectoryString = tableDirectory.toString();
			Table table = new Table(tableDirectoryString);
			tables.put(table.getTableName(), table);
		}
		return true;
	}
	
	public static Table getTable(String tableName) {
		return tables.get(tableName);
	}
	static {
		try {
			loadTables();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
