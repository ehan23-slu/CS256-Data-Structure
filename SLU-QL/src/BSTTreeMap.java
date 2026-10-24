import java.util.ArrayList;
import java.util.TreeMap;

public class BSTTreeMap {
    // HINT FROM OFFICE HOUR: USE TREE MAP
    private TreeMap<String, Table> tree;
    public BSTTreeMap() {
        tree = new TreeMap<>();
    }
    public Table getTable(String tableName) {
        return tree.get(tableName);
    }

    public void createTable(String tableName, int numCols, ArrayList<String> colTypes, ArrayList<String> colNames) {
        // create new table
        Table newTable = new Table(tableName, numCols, colNames, colTypes);
        tree.put(tableName, newTable);
        // Print columns
        System.out.print("New table " + tableName + " with column(s) ");
        for (int i = 0; i < colNames.size(); i++) {
            System.out.print(colNames.get(i));
            if (i < colNames.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println(" created");
    }

    public void removeTable(String tableName) {
        // No table are there
        if (!tree.containsKey(tableName)) {
            System.out.println("Error: " + tableName + " does not name a table in the database");
            return;
        }
        // remove
        tree.remove(tableName);
        System.out.println("Table " + tableName + " deleted");
    }

}
