package uniandes.dpoo.proyecto1.queries;

import java.util.List;

import joinery.DataFrame;
import uniandes.dpoo.proyecto1.table.Table;
import uniandes.dpoo.proyecto1.tablesmanager.TablesManager;

public class CreditCardQueries {
	private static final Table creditCardTable = TablesManager.getTable("credit_card");
	
	public static boolean isCreditCardBlocked(String creditCardId) {
		DataFrame<String> resultDf = creditCardTable.searchEqualValues("credit_card_id", creditCardId);
		int isBlockedIndex = creditCardTable.getColumnIndex("is_blocked");
		boolean isBlocked = resultDf.get(0, isBlockedIndex).equals("true");
		return isBlocked;
	}
	
    public static void changeCarStatusTo(String creditCardId, boolean status) {
    	DataFrame<String> resultDf = creditCardTable.searchEqualValues("credit_card_id", creditCardId);
    	creditCardTable.changeRowColumnValue(resultDf, "status_id", String.valueOf(status));
    }
    
    public static List<String> getAllCreditCardId() {
    	return creditCardTable.getAllColumn("credit_card_id");
    }
    
    public static void addCreditCard(String[] creditCardInfo) {
    	creditCardTable.addRow(creditCardInfo);
    }
}
