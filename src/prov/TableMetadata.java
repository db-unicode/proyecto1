package prov;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import uniandes.dpoo.proyecto1.table.ForeignKeyReference;

public class TableMetadata {

	private Map<String, Object> jsonHashMap;
	private Map<String, String> columnType;
	private Map<String, ForeignKeyReference> foreignKeys;
	private List<String> primaryKeys;

	public TableMetadata(String jsonFilePath) throws Exception {
		this.jsonHashMap = JSONManager.loadJSON(jsonFilePath);
		this.columnType = new LinkedHashMap<String, String>();
		this.foreignKeys = new LinkedHashMap<String, ForeignKeyReference>();
		this.primaryKeys = new ArrayList<String>();
		deserializeJSON();
	}

	private void deserializeJSON() {
		for (Map.Entry<String, Object> entry : this.jsonHashMap.entrySet()) {
			String columnName = entry.getKey();
			@SuppressWarnings("unchecked")
			Map<String, Object> columnInfo = (Map<String, Object>) entry.getValue();
			addColumnType(columnName, columnInfo);
			addPrimaryKey(columnName, columnInfo);
			addForeignKey(columnName, columnInfo);
		}
	}

	private void addColumnType(String columnName, Map<String, Object> columnInfo) {
		String columnType = (String) columnInfo.get("type");
		this.columnType.put(columnName, columnType);
	}

	private void addPrimaryKey(String columnName, Map<String, Object> columnInfo) {
		if (columnInfo.containsKey("primaryKey")) {
			this.primaryKeys.add(columnName);
		}
	}

	private void addForeignKey(String columnName, Map<String, Object> columnInfo) {
		if (columnInfo.containsKey("foreignKey")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> foreignKeyInfo = (Map<String, Object>) columnInfo.get("foreignKey");
			String referenceTable = (String) foreignKeyInfo.get("referenceTable");
			String referenceColumn = (String) foreignKeyInfo.get("referenceColumn");
			ForeignKeyReference foreignKeyReference = new ForeignKeyReference(referenceTable, referenceColumn);
			this.foreignKeys.put(columnName, foreignKeyReference);
		}
	}

	public Map<String, String> getColumnType() {
		return columnType;
	}

	public Map<String, ForeignKeyReference> getForeignKeys() {
		return foreignKeys;
	}

	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

}
