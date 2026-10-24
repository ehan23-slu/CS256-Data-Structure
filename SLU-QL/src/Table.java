import java.util.*;

public class Table {

    private String name;
    private ArrayList<Row> rows;
    private int numCols;
    private ArrayList<String> colNames;
    private ArrayList<String> colTypes;

    private String indexType;
    private String indexCol;
    private Map<TableEntry, ArrayList<Integer>> hashIdx;
    private TreeMap<TableEntry, ArrayList<Integer>> bstIndex;


    public Table(String name, int numCols, ArrayList<String> colNames, ArrayList<String> colTypes) {
        this.name = name;
        this.rows = new ArrayList<>();
        this.numCols = numCols;
        this.colNames = colNames;
        this.colTypes = colTypes;

        this.indexType = null;
        this.hashIdx = null;
        this.bstIndex = null;
        this.indexCol = null;
    }

    private void deleteRow(int i) {
        rows.remove(i);
    }
    public ArrayList<Row> getRows() {
        return rows;
    }

    public ArrayList<String> getColNames() {
        return colNames;
    }

    // get TableEntry
    public TableEntry getValues(String colName, String data) {
        // get the column type
        String colType = colTypes.get(colNames.indexOf(colName));
        TableEntry value = null;
        // type checking
        switch (colType.charAt(0)) {
            case 'b': // boolean
                value = new TableEntry.BoolTableEntry(Boolean.parseBoolean(data));
                break;
            case 's': // string
                value = new TableEntry.StringTableEntry(data);
                break;
            case 'd': // double
                value = new TableEntry.DoubleTableEntry(Double.parseDouble(data));
                break;
            case 'i': // integer
                value = new TableEntry.IntTableEntry(Integer.parseInt(data));
                break;
        }
        return value;
    }

    // add rows
    public void addRow(Row row) {
        rows.add(row);
        if (indexType == null) {
            return;
        }

        // get value from column
        int idx = getColumnIndex(indexCol);
        TableEntry value = row.getCol(idx);

        // hash index
        if (indexType.equals("hash")) {
            ArrayList<Integer> rowIds = hashIdx.getOrDefault(value, new ArrayList<>());
            rowIds.add(rows.size() - 1);
            hashIdx.put(value, rowIds);
        }

        // bst index
        else if (indexType.equals("bst")) {
            ArrayList<Integer> rowIds = bstIndex.getOrDefault(value, new ArrayList<>());
            rowIds.add(rows.size() - 1);
            bstIndex.put(value, rowIds);
        }
    }

    public void printFrom(String colToCompare, Comparator<TableEntry> cmp, TableEntry val, ArrayList<String> colsToPrint, String op, boolean QMode) {
        // column that has the condition
        int colIdxToCompare = colNames.indexOf(colToCompare);
        int rowCount = 0; // to track number of rows to be printed
        if (!QMode) {
            for (String col : colsToPrint) {
                System.out.print(col + " ");
            }
            System.out.print("\n");
        }

        ArrayList<Integer> colIdxToPrint = nameIdx(colsToPrint);
        ArrayList<Integer> Rows = null;

        // Check if an index exists on the conditional column and short-circuit
        if ((hash() || hash()) && indexCol.equals(colToCompare)) {
            // If hash index is available and the operation is equality ("="), retrieve rows that match from the hash index
            if (hash() && op.equals("=")) {
                Rows = hashIdx.get(val);
            } else if (bst()) {
                // If a bst, index is available
                if (op.equals("=")) {
                    Rows = bstIndex.get(val);
                } else {
                    // For other operations (<, >), filter rows based on the comparison
                    Rows = getFrom(val, cmp);
                }

                // If some matching rows are found, proceed to sort them
                if (Rows != null) {
                    // Sort the matching rows based on the order of the bst index
                    Rows.sort((r1, r2) ->
                            cmp.compare(rows.get(r1).getEntry(getColumnIndex(colToCompare)), rows.get(r2).getEntry(getColumnIndex(colToCompare))));
                }
            }
        }

        if (Rows != null) {
            for (int i : Rows) {
                // if quiet mode
                if (!QMode) {
                    // print actual rows
                    for (int colIdx : colIdxToPrint) {
                        System.out.print(rows.get(i).getEntry(colIdx) + " ");
                    }
                    System.out.print("\n");
                }
                rowCount++; // increment
            }
        }

        else if ((!bst() && !hash()) || (hash() && !op.equals("=")) || !indexCol.equals(colToCompare)) {
            for (Row r : rows) {
                // compare value
                if (cmp.compare(r.getEntry(colIdxToCompare), val) == 0){
                    // only print the actual row if quiet mode is not on
                    if (!QMode) {
                        for (int colIdx : colIdxToPrint) {
                            System.out.print(r.getEntry(colIdx) + " ");
                        }
                        System.out.print("\n");
                    }
                    rowCount++;
                }
            }
        }
        System.out.print("Printed " + rowCount + " matching rows from " + name + "\n");
    }

    private int getColumnIndex(String colName) {
        return colNames.indexOf(colName);
    }

    // no condition - print ALL
    public void printAll(ArrayList<String> colsToPrint, boolean QMode) {
        // column index
        ArrayList<Integer> cols = nameIdx(colsToPrint);
        int rowCount = 0;

        if (!QMode) {
            // print the name of the columns
            for (String col : colsToPrint) {
                System.out.print(col + " ");
            }
            System.out.println();
        }

        // print the data
        for (int row = 0; row < rows.size(); row++) {
            if (!QMode) {
                for (int col : cols) {
                    System.out.print(getRowColumn(row, col).toString() + " ");
                }
                System.out.print("\n");
            }
            rowCount++;
        }
        System.out.println("Printed " + rowCount + " matching rows from " + name);
    }

    private TableEntry getRowColumn(int row, int col) {
        return rows.get(row).getEntry(col);
    }

    // get the index of columns
    private ArrayList<Integer> nameIdx(ArrayList<String> cols) {
        ArrayList<Integer> idx = new ArrayList<>();
        for (String col : cols) {
            idx.add(colNames.indexOf(col));
        }
        return idx;
    }

    public boolean bst() {
        return bstIndex != null;
    }

    public boolean hash() {
        return hashIdx != null;
    }

    public void deleteFrom(String colName, Comparator<TableEntry> cmp, TableEntry value, String op) {
        // column with condition
        int colIdx = getColumnIndex(colName);
        // Error
        if (colIdx == -1) {
            System.out.println("Error: " + colName + " does not name a column in  " + name);
            return;
        }
        int numRowsDeleted = 0;
        for (int i = rows.size()-1; i >= 0 ; i--) {
            TableEntry cellVal = getRowColumn(i, colIdx);
            if (cmp.compare(cellVal, value) == 0) {
                // delete if it matches with conditions
                deleteRow(i);
                numRowsDeleted++;
            }
        }
        // regenerate index
        if (hash()) {
            hashIdx = null;
            HashIndex(colName);
        }
        if (bst()) {
            bstIndex = null;
            bstIdx(colName);
        }

        System.out.println("Deleted " + numRowsDeleted + " rows from " + name);
    }

    public void join(Table t2, Comparator<TableEntry> cmp, String table1col, String table2col, ArrayList<String> colsToPrint, ArrayList<Boolean> printFromTable1, boolean QMode) {
        ArrayList<Integer> colIdxToPrint = new ArrayList<>(colsToPrint.size());
        int rowCount = 0;

        for (int i = 0; i < colsToPrint.size(); i++) {
            // ternary operator (condition) ? true expression : false expression
            ArrayList<String> targetTableColNames = printFromTable1.get(i) ? colNames : t2.colNames;
            colIdxToPrint.add(targetTableColNames.indexOf(colsToPrint.get(i)));
        }
        // get the column index to compare
        int table1Idx = colNames.indexOf(table1col);
        int table2Idx = t2.colNames.indexOf(table2col);

        if (!QMode) {
            for (String col : colsToPrint) {
                System.out.print(col + " ");
            }
            System.out.println();
        }

        // Make Fast Way
        Map<TableEntry, ArrayList<Row>> hashTableTwo = new HashMap<>();
        for (Row r2 : t2.getRows()) {
            TableEntry entry = r2.getEntry(table2Idx);
            ArrayList<Row> rowList = hashTableTwo.getOrDefault(entry, new ArrayList<>());
            rowList.add(r2);
            hashTableTwo.put(entry, rowList);
        }
        // loop through tables and compare
        for (Row r1 : rows) {
            TableEntry joinValue = r1.getEntry(table1Idx);
            if (hashTableTwo.containsKey(joinValue)) {
                for (Row r2 : hashTableTwo.get(joinValue)) {
                    // rows match
                    rowCount++;
                    if (!QMode) {
                        printFromTwoRows(r1, r2, colIdxToPrint, printFromTable1);
                    }
                }
            }
        }
        System.out.print("Printed " + rowCount + " rows from joining " + name + " to " + t2.name + "\n");
    }

    public void generateIndex(String tableName, String type, String colName) {
        // existing index should be cleared
        if (hash() || bst()) {
            hashIdx = null;
            bstIndex = null;
            indexCol = null;
            indexType = null;
        }
        // hash index
        if (type != null && type.equals("hash")) {
            HashIndex(colName);
            System.out.println("Created hash index for table " + tableName + " on column " + colName);
        }
        // bst index
        else if (type != null && type.equals("bst")) {
            bstIdx(colName);
            System.out.println("Created bst index for table " + tableName + " on column " + colName);
        }
        else {
            System.out.println("Error: " + type + " it shoule be either hash or bst");
        }
    }

    private void HashIndex(String colName) {
        Map<TableEntry, ArrayList<Integer>> index = new HashMap<>();
        int colIdx = getColumnIndex(colName);

        for (int i = 0; i < rows.size(); i++) {
            // get the value from each column and row
            // indexing or create new index
            TableEntry entry = rows.get(i).getEntry(colIdx);
            ArrayList<Integer> rowIds = index.getOrDefault(entry, new ArrayList<>());
            // add index of row
            rowIds.add(i);
            index.put(entry, rowIds);
        }
        indexType = "hash";
        hashIdx = index;
        indexCol = colName;
    }

    private void bstIdx(String colName) {
        TreeMap<TableEntry, ArrayList<Integer>> index = new TreeMap<>();
        // indexing column
        int colIdx = colNames.indexOf(colName);
        // loop through to get value from each row
        // same logic with createHashIndex
        for (int i = 0; i < rows.size(); i++) {
            TableEntry entry = rows.get(i).getEntry(colIdx);
            ArrayList<Integer> rowIds = index.getOrDefault(entry, new ArrayList<>());
            rowIds.add(i);
            index.put(entry, rowIds);
        }
        indexType = "bst";
        bstIndex = index;
        indexCol = colName;
    }

    private void printFromTwoRows(Row r1, Row r2, ArrayList<Integer> colIdxToPrint, ArrayList<Boolean> colFrom1) {
        // loop through the columns
        for (int i = 0; i < colIdxToPrint.size(); i++) {
            if (colFrom1.get(i)) { // from table 1
                System.out.print(r1.getEntry(colIdxToPrint.get(i)) + " ");
            }
            else { // from table 2
                System.out.print(r2.getEntry(colIdxToPrint.get(i)) + " ");
            }
        }
        System.out.println();
    }
    // print with condition
    private ArrayList<Integer> getFrom(TableEntry value, Comparator<TableEntry> cmp) {
        ArrayList<Integer> result = new ArrayList<>();
        // look through entry with compare
        for (Map.Entry<TableEntry, ArrayList<Integer>> entry : bstIndex.entrySet()) {
            if (cmp.compare(entry.getKey(), value) == 0) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }

}
