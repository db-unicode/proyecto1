package uniandes.dpoo.proyecto1.table;

public class ForeignKeyReference {
    private String referenceTable;   
    private String referenceColumn;  

    public ForeignKeyReference(String referenceTable, String referenceColumn) {
        this.referenceTable = referenceTable;
        this.referenceColumn = referenceColumn;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public String getReferenceColumn() {
        return referenceColumn;
    }

    @Override
    public String toString() {
        return referenceTable + "(" + referenceColumn + ")";
    }
}


