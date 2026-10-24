import java.util.ArrayList;

public class Row {
    private ArrayList<TableEntry> row;

    // constructor that initializes the row
    public Row(ArrayList<TableEntry> data) {
        this.row = data;
    }
    // get a TableEntry at a specific index
    public TableEntry getEntry(int index) {
        return row.get(index);
    }
    // get a TableEntry by column index
    public TableEntry getCol(int col) {
        return row.get(col);
    }
}
