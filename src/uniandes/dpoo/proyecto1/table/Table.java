package uniandes.dpoo.proyecto1.table;

import joinery.DataFrame;
import prov.CSVManager;
import prov.TableMetadata;
import prov.TablePathResolver;
import prov.TablesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.time.LocalTime;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.sequence.DeleteCommand;

public class Table {

	private String tableDirectoryPath;
	private DataFrame<Object> df;
	private TableMetadata metadata;
	private String tableName;
	private List<String> columns;
	private List<Integer> primaryKeysIndex;
	private int totalColumns;

	public Table(String tableDirectoryPath) throws Exception {
		String csvFilePath = TablePathResolver.getCSVFilePath(tableDirectoryPath);
		String jsonFilePath = TablePathResolver.getJSONFilePath(tableDirectoryPath);

		this.tableDirectoryPath = tableDirectoryPath;
		this.tableName = TablePathResolver.getTableName(tableDirectoryPath);

		this.df = CSVManager.loadCSV(csvFilePath);
		this.columns = CSVManager.getColumnNames(csvFilePath);
		this.totalColumns = this.columns.size();

		this.metadata = new TableMetadata(jsonFilePath);

		this.primaryKeysIndex = new ArrayList<Integer>();
		this.setPrimaryKeysIndex();
		this.castAllColumns();
	}

	public void deleteIdRow(String idString) {
		DataFrame<String> result = getStringedDataFrame();
		result = result.select(row -> row.get(0).equals(idString));
		deleteRow(result);
	}
	public Table addRow(String... values) {
		if (values.length != totalColumns) {
			throw new IllegalArgumentException(
					"Number of provided values doesn't match the number of columns in the table.");
		}
		this.checkUniquePrimaryKeyCombination(values);
		df.append(Arrays.asList(values));
		/*
		 * inefficient solution to make the entered data be converted to the type of its
		 * corresponding column. The correct way is to make a function that iterates
		 * over String... values array using the index i.e. for (i = 0, i <
		 * values.size(), i++) this to access the Array columns and get the column name
		 * and then with the column name get from the metadata the datatype that
		 * corresponds to that column and do the corresponding casting of the values[i],
		 * and then add each cast value to a Object[].
		 * 
		 */
		this.saveCSVDataFrame();
		this.castAllColumns();
		return this;
	}
	
	private void checkUniquePrimaryKeyCombination(String[] values) {
	    DataFrame<String> df_string = getStringedDataFrame();
	    for (List<String> row : df_string) {
	        if (hasSamePrimaryKeysCombination(row, values)) {
	            throw new IllegalArgumentException("Values " + Arrays.toString(values) + " do not meet the condition of unique primary keys of the table " + this.tableName);
	        }
	    }
	}

	public DataFrame<String> getStringedDataFrame() {
	    return this.df.apply(v -> v == null ? null : v.toString());
	}

	private boolean hasSamePrimaryKeysCombination(List<String> row, String[] values) {
	    for (int primaryKeyIndex : this.primaryKeysIndex) {
	        String rowValue = row.get(primaryKeyIndex);
	        String valueToCheck = values[primaryKeyIndex];
	        if (!rowValue.equals(valueToCheck)) {
	            return false;
	        }
	    }
	    return true;
	}

	private void castAllColumns() {
		for (Map.Entry<String, String> entry : this.metadata.getColumnType().entrySet()) {
			String columnName = entry.getKey();
			String targeType = entry.getValue();
			this.castColumn(columnName, targeType);
		}
	}

	private void castColumn(String columnName, String targetType) {
		checkColumnExists(columnName);
		List<Object> columnData = this.df.col(columnName);
		List<Object> transformedData;

		switch (targetType) {
		case "String":
			transformedData = columnData.stream().map(Object::toString).collect(Collectors.toList());
			break;
		case "Integer":
			transformedData = columnData.stream().map(val -> Integer.parseInt(val.toString()))
					.collect(Collectors.toList());
			break;
		case "Boolean":
			transformedData = columnData.stream().map(val -> Boolean.parseBoolean(val.toString()))
					.collect(Collectors.toList());
			break;
		case "LocalDate":
			transformedData = columnData.stream().map((Object val) -> LocalDate.parse(val.toString().replace('_', '-')))
					.collect(Collectors.toList());

			break;
		case "LocalTime":
			transformedData = columnData.stream().map(val -> LocalTime.parse(val.toString()))
					.collect(Collectors.toList());
			break;
		default:
			throw new IllegalArgumentException("Unsupported conversion type: " + targetType);
		}

		this.df = this.df.drop(columnName).add(columnName, transformedData);
	}

	private void checkColumnExists(String columnName) throws IllegalArgumentException {
		if (this.columns.indexOf(columnName) == -1) {
			throw new IllegalArgumentException(
					"Column " + columnName + " do not belong to " + this.tableName + " Table");
		}
	}

	private void setPrimaryKeysIndex() {
		for (String primaryKeyColumnName : this.metadata.getPrimaryKeys()) {
			int primaryKeyIndex = this.columns.indexOf(primaryKeyColumnName);
			this.primaryKeysIndex.add(primaryKeyIndex);
		}
	}

	private void saveCSVDataFrame() {
		DataFrame<String> stringedDataframe = this.getStringedDataFrame();
		try {
			stringedDataframe.writeCsv(TablePathResolver.getCSVFilePath(this.tableDirectoryPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DataFrame<String> searchEqualValues(String... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("Odd number of arguments. Column-value pairs are expected.");
        }
        DataFrame<String> resultDf = this.getStringedDataFrame();
        for (int i = 0; i < values.length; i += 2) {
            String column = values[i];
            String value = values[i + 1];
            checkColumnExists(column);
            int columnIndex = this.getColumnIndex(column);;
            resultDf = resultDf.select(row -> row.get(columnIndex).equals(value));
        }
        if (resultDf.isEmpty()) {
        	String msg = "The query " + values.toString() + " in the table '" + this.tableName + "' is empty";
        	throw new IllegalArgumentException(msg);
        }
        return resultDf;
	}
	
	public void deleteRow(DataFrame<String> onlyOneRowDataFrame) {
		int index = this.getRowIndex(onlyOneRowDataFrame);
		df.drop(index);
		this.saveCSVDataFrame();
		this.castAllColumns();
	}
	
	public void changeRowColumnValue(DataFrame<String> onlyOneRowDataFrame, String columnName, String newValue) {
		int index = this.getRowIndex(onlyOneRowDataFrame);
		int columnId = this.getColumnIndex(columnName);
		df.set(index, columnId, newValue);
		this.saveCSVDataFrame();
		this.castAllColumns();
	}
	
	public int getColumnIndex(String columnName) {
		return columns.indexOf(columnName);
	}
	
	public DataFrame<Object> getDataFrame() {
		return df;
	}

	public String getTableName() {
		return tableName;
	}

	public String getCsvFilePath() {
		return tableDirectoryPath;
	}

	public List<String> getColumns() {
		return columns;
	}

	public TableMetadata getMetadata() {
		return metadata;
	}
	private int getRowIndex(DataFrame<String> onlyOneRowDataFrame) {
		return (int) onlyOneRowDataFrame.index().iterator().next();
	}
}
