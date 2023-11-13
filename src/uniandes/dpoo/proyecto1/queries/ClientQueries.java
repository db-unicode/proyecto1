package uniandes.dpoo.proyecto1.queries;


import java.util.List;


import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class ClientQueries {
	private static final Table clientTable = TablesManager.getTable("client");

	private ClientQueries() {
	}

	public static String checkClientId(String clientId) {
		String[] query = { "client_id", clientId };
		clientTable.searchEqualValues(query);
		return clientId;
	}
	
	public static String checkClientCreditCardId(String clientId) {
		String[] query = { "client_id", clientId };
		int creditCardIdIndex = clientTable.getColumnIndex("credit_card_id");
		DataFrame<String> result = clientTable.searchEqualValues(query);
		String creditCardId = result.get(0, creditCardIdIndex);
		return creditCardId;
	}
	
	public static List<String> getAllClientId() {
		return clientTable.getAllColumn("client_id");
	}
	
	public static String getClientName(String clientId) {
		String[] query = { "client_id", clientId };
		int clientNameIndex = clientTable.getColumnIndex("client_name");
		DataFrame<String> result = clientTable.searchEqualValues(query);
		String clientName = result.get(0, clientNameIndex);
		return clientName;
	}
	
	public static void addClient(String[] clientInfo) {
		clientTable.addRow(clientInfo);
	}
}
