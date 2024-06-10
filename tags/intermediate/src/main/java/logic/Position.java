package logic;

/**
 * Position Class for every GameTile placement
 *
 * @author Jonathan El Jusup (cgt104707)
 */
public class Position {

    private final int column;
    private final int row;

    /**
     * Position Constructor
     *
     * @param column Column
     * @param row Row
     */
    public Position(int column, int row) {
        assert column >= 0;
        assert row >= 0;

        this.column = column;
        this.row = row;
    }

    /**
     * Public Getter for getting Column of Position Class
     *
     * @return Column of Position
     */
    public int getColumn() {
        return column;
    }

    /**
     * Public Getter for getting Row of Position Class
     *
     * @return Row of Position
     */
    public int getRow() {
        return row;
    }

    /**
     * Comparator of Position Classes to determine, if Positions are equal
     * Compares Instance Type, Column and Row
     *
     * @param obj Object to compare with
     * @return is Position & same Column & same Row -> TRUE ; else FALSE
     */
    @Override
    public boolean equals(Object obj) {
        Position toCompare = obj instanceof Position ? (Position) obj : null;
        return toCompare != null && this.column == toCompare.getColumn() && this.row == toCompare.getRow();
    }

    /**
     * String representation of Position Class. Output is Column and Row of Position
     *
     * @return Column & Row of Position
     */
    @Override
    public String toString() {
        return "Position: " + "Column = " + column + " | Row = " + row;
    }
}
