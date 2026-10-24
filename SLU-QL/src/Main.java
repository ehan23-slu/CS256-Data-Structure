import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // create a database
        BSTTreeMap db = new BSTTreeMap();
        // Scanner to read
        Scanner in = new Scanner(System.in);
        // for Quiet Mode
        boolean QMode = false;

        // prompt the user
        System.out.print("% ");

        // loop while there is more data to read
        while (in.hasNext()) {
            // read the next string
            String cmd = in.next();

            switch (cmd.charAt(0)) {
                case '#':
                    // just read next line
                    in.nextLine();
                    break;

                case 'Q': // QUIT
                    // print the given closing message
                    System.out.println("Goodbye, SLU!");
                    System.exit(0);
                    break;

                case 'C': // CREATE
                    // create a new table
                    String tableName = in.next();
                    // Possible Error
                    if (db.getTable(tableName) != null) {
                        System.out.println("Error: table is already existing " + tableName);
                        in.nextLine();
                        break;
                    }

                    // number of columns
                    int numCols = Integer.parseInt(in.next());
                    // column types and names putting them into ArrayList
                    ArrayList<String> colTypes = new ArrayList<>();
                    for (int i = 0; i < numCols; i++) {
                        colTypes.add(in.next());
                    }
                    ArrayList<String> colNames = new ArrayList<>();
                    for (int i = 0; i < numCols; i++) {
                        colNames.add(in.next());
                    }
                    // create new table
                    db.createTable(tableName, numCols, colTypes, colNames);
                    break;

                case 'R': // REMOVE
                    db.removeTable(in.next());
                    break;

                case 'I': // INSERT
                    in.next(); // skip 'INTO'
                    tableName = in.next(); // get table name
                    Table table = db.getTable(tableName); // put names on database
                    // get number of rows
                    int numRows = Integer.parseInt(in.next());
                    if (table == null) {
                        System.out.println("Error: " + tableName + " does not name a table in the database");
                        for (int i = 0; i < numRows; i++) {
                            in.nextLine();
                        }
                        in.nextLine();
                        break;
                    }
                    in.next(); // skip 'ROWS'

                    // loop through the rows
                    int start = table.getRows().size();
                    for (int i = 0; i < numRows; i++) {
                        ArrayList<TableEntry> newEntries = new ArrayList<>();
                        // loop through to add data
                        for (String s : table.getColNames()) {
                            TableEntry value = table.getValues(s, in.next());
                            newEntries.add(value);
                        }
                       // add the rows
                        Row data = new Row(newEntries);
                        table.addRow(data);
                    }
                    // where last row was added
                    int end = table.getRows().size() - 1;
                    System.out.println("Added " + numRows + " rows to " + tableName + " from position " + start + " to " + end);
                    break;

                case 'P': // PRINT
                    // skip 'FROM'
                    in.next();
                    // get table name
                    tableName = in.next();
                    // put it at the database
                    table = db.getTable(tableName);

                    // Possible Error
                    if (table == null) {
                        System.out.println("Error: " + tableName + " does not name a table in the database");
                        // read next cmd
                        in.nextLine();
                        break;
                    }
                    // get columns to print out
                    numCols = Integer.parseInt(in.next());
                    ArrayList<String> colsToPrint = new ArrayList<>();
                    // loop through the column
                    for (int i = 0; i < numCols; i++) {
                        String col = in.next();
                        if (!table.getColNames().contains(col)) {
                            // Error
                            System.out.println("Error: " + col + " does not name a column in " + tableName);
                            break;
                        }
                        // add column
                        colsToPrint.add(col);
                    }

                    if (colsToPrint.size() != numCols) {
                        in.nextLine();
                        break;
                    }

                    // print with condition
                    if (in.next().charAt(0) == 'W') {
                        String colToCompare = in.next(); // get condition
                        String op = in.next(); // condition which are one of (=, <, >)
                        String data = in.next(); // get the target to compare
                        if (!table.getColNames().contains(colToCompare)) {
                            // Error
                            System.out.printf("Error: %s does not name a column in %s\n", colToCompare, tableName);
                            break;
                        }
                        // get correct type of comparator
                        Comparator<TableEntry> cmp;
                        switch (op) {
                            case "=":
                                cmp = new RowComparators.EqualComparator();
                                break;
                            case "<":
                                cmp = new RowComparators.LTComparator();
                                break;
                            default:
                                // greater than
                                cmp = new RowComparators.GTComparator();
                                break;
                        }
                        TableEntry value = table.getValues(colToCompare, data);
                        table.printFrom(colToCompare, cmp, value, colsToPrint, op, QMode);

                    } else {
                        // no condition
                        table.printAll(colsToPrint, QMode);
                    }
                    break;

                case 'D': // DELETE
                    // skip 'FROM'
                    in.next();
                    // get tableName
                    tableName = in.next();
                    in.next();
                    // read the condition
                    String colName = in.next();
                    String op = in.next();
                    String data = in.next();
                    table = db.getTable(tableName);

                    // Errors
                    if (!table.getColNames().contains(colName)) {
                        System.out.println("Error: " + colName + " does not name a column in " + tableName);
                        in.nextLine();
                        break;
                    }
                    if (table == null) {
                        System.out.println("Error: " + tableName + " does not name a table in the database");
                        in.nextLine();
                        break;
                    }
                    TableEntry value = table.getValues(colName, data);
                    Comparator<TableEntry> cmp;

                    // supported comparator
                    switch (op) {
                        case "=":
                            cmp = new RowComparators.EqualComparator();
                            break;
                        case "<":
                            cmp = new RowComparators.LTComparator();
                            break;
                        default: // greater than
                            cmp = new RowComparators.GTComparator();
                            break;
                    }
                    table.deleteFrom(colName, cmp, value, op);
                    break;

                case 'G': // GENERATE
                    // skip 'FROM'
                    in.next();
                    // get tableName
                    tableName = in.next();
                    // get type
                    // hash or bst
                    String type = in.next();
                    // skip 'INDEX' and 'ON'
                    in.next();
                    in.next();
                    // column to indexing
                    colName = in.next();
                    table = db.getTable(tableName);

                    // Possible Errors
                    if (!table.getColNames().contains(colName)) {
                        System.out.println("Error: " + colName + " does not name a column in " + tableName);
                        in.nextLine();
                        break;
                    }
                    if (table == null) {
                        System.out.println("Error: " + tableName + " does not name a table in the database");
                        in.nextLine();
                        break;
                    }
                    table.generateIndex(tableName, type, colName);
                    break;

                case 'J': // JOIN
                    String table1 = in.next();
                    in.next(); // skip 'AND'
                    String table2 = in.next();

                    // get tables to join each other
                    Table t1 = db.getTable(table1);
                    Table t2 = db.getTable(table2);
                    in.next();

                    // column from table 1 to join
                    String col1 = in.next();
                    // Error
                    if (!t1.getColNames().contains(col1)) {
                        System.out.printf("Error: %s does not name a column in %s\n", col1, table1);
                        in.nextLine();
                        break;
                    }
                    in.next();

                    // column from table 2 to join
                    String col2 = in.next();
                    // Error
                    if (!t2.getColNames().contains(col2)) {
                        System.out.printf("Error: %s does not name a column in %s\n", col2, table2);
                        in.nextLine();
                        break;
                    }
                    in.next();
                    in.next();
                    // columns to print
                    numCols = Integer.parseInt(in.next());
                    colsToPrint = new ArrayList<>();
                    ArrayList<Boolean> colFrom1 = new ArrayList<>();

                    // get columns
                    for (int i = 0; i < numCols; i++) {
                        // get column name
                        String column = in.next();

                        int tableIndicator = Integer.parseInt(in.next()); // either table 1 or table 2

                        if (tableIndicator == 1) { // from table 1
                            // possible error checking - does it exist
                            if (t1.getColNames().contains(column)) {
                                colsToPrint.add(column);
                                colFrom1.add(true); // when it from table 1
                            } else {
                                // Error
                                System.out.printf("Error: %s does not name a column in %s\n", column, table1);
                                break;
                            }
                        }

                        else if (tableIndicator == 2) { // from table 2
                            // possible error checking - does it exist
                            if (t2.getColNames().contains(column)) {
                                colsToPrint.add(column);
                                colFrom1.add(false); // when if from table 2
                            } else {
                                // Error
                                System.out.printf("Error: %s does not name a column in %s\n", column, table2);
                                break;
                            }
                        }
                    }
                    Comparator<TableEntry> joinComparator = new RowComparators.EqualComparator();
                    t1.join(t2, joinComparator, col1, col2, colsToPrint, colFrom1, QMode);
                    break;

                default:
                    // print error message using print out, not print err
                    System.out.println("Error: unrecognized command");
                    in.nextLine();
            }
            // re-prompt for the next command
            System.out.print("% ");
        }
    }
}
