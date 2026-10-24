public abstract class TableEntry implements Comparable<TableEntry> {

    private String value;
    private Type type;

    public enum Type {
        BOOL,
        DOUBLE,
        INT,
        STRING
    }

    // constructor
    public TableEntry(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    // toString method for printing object
    public String toString() {
        return value;
    }

    // methods for getting what we need such as value, type, etc
    public String getValue() {
        return value;
    }
    public abstract Type getType();

    public abstract int compareTo(TableEntry o);

    // convert string to specific type
    public static TableEntry stringToTE(String next, Type colType) {
        switch (colType) {
            case BOOL:
                if (next.equalsIgnoreCase("true")) {
                    return new BoolTableEntry(true);
                } else if (next.equalsIgnoreCase("false")) {
                    return new BoolTableEntry(false);
                } else {
                    throw new IllegalArgumentException("Invalid input: " + next);
                }
            case DOUBLE:
                try {
                    return new DoubleTableEntry(Double.parseDouble(next));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error: Invalid input " + next, e);
                }
            case INT:
                if (!isNumeric(next)) {
                    throw new NumberFormatException("Error: Invalid input " + next);
                }
                return new IntTableEntry(Integer.parseInt(next));
            case STRING:
                return new StringTableEntry(next);
            default:
                throw new IllegalArgumentException("Error: Unknown Type " + colType);
        }
    }

    // method to check whether numeric data or not
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("-?\\d+");
    }

    // TableEntry for Boolean
    public static class BoolTableEntry extends TableEntry {

        private boolean data;

        public BoolTableEntry(boolean b) {
            super(Boolean.toString(b), Type.BOOL); // Call the constructor of TableEntry
            data = b;
        }

        @Override
        public Type getType() {
            return Type.BOOL;
        }

        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.BOOL) {
                throw new ClassCastException("Cannot compare Bool with " + o.getType());
            }
            BoolTableEntry ob = (BoolTableEntry) o;
            return Boolean.compare(data, ob.data);
        }

    }

    // TableEntry for Double
    public static class DoubleTableEntry extends TableEntry {

        private double data;

        public DoubleTableEntry(double d) {
            super(Double.toString(d), Type.DOUBLE); // Call the constructor of TableEntry
            data = d;
        }

        @Override
        public Type getType() {
            return Type.DOUBLE;
        }

        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.DOUBLE) {
                throw new ClassCastException("Cannot compare Double with " + o.getType());
            }
            DoubleTableEntry ob = (DoubleTableEntry) o;
            return Double.compare(data, ob.data);
        }
    }

    // TableEntry for Int
    public static class IntTableEntry extends TableEntry {

        private int data;

        public IntTableEntry(int i) {
            super(Integer.toString(i), Type.INT); // Call the constructor of TableEntry
            data = i;
        }

        @Override
        public Type getType() {
            return Type.INT;
        }

        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.INT) {
                throw new ClassCastException("Cannot compare Int with " + o.getType());
            }
            IntTableEntry ob = (IntTableEntry) o;
            return Integer.compare(data, ob.data);
        }
    }

    // TableEntry for String
    public static class StringTableEntry extends TableEntry {

        private String data;

        public StringTableEntry(String s) {
            super(s, Type.STRING); // Call the constructor of TableEntry
            data = s;
        }

        @Override
        public Type getType() {
            return Type.STRING;
        }

        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.STRING) {
                throw new ClassCastException("Cannot compare String with " + o.getType());
            }
            StringTableEntry ob = (StringTableEntry) o;
            return data.compareTo(ob.data);
        }
    }
}
