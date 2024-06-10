package logic;

/**
 * Position Record for getting/placing GameTiles on gameBoard.
 * Implements Comparable Interface to compare Positions.
 *
 * @param column Column
 * @param row    Row
 * @author Jonathan El Jusup (cgt104707)
 */
public record Position(int column, int row) implements Comparable<Position> {

    /**
     * Column Getter.
     *
     * @return Column
     */
    @Override
    public int column() {
        return column;
    }

    /**
     * Row Getter.
     *
     * @return Row
     */
    @Override
    public int row() {
        return row;
    }

    /**
     * String representation of Position Class. Output is Column and Row of Position
     *
     * @return Column & Row of Position
     */
    @Override
    public String toString() {
        return String.format("(%d|%d)", column, row);
    }

    /**
     * Custom compareTo Method. Compares Columns and Rows. A Position is lower,
     * the lower the Row is. If same Row, then position is lower, the lower the
     * Column is. If same Column and same Row, both positions are equal.
     *
     * @param pos the Position to be compared.
     * @return -1 (lower) | 0 (equal) | 1 (higher)
     */
    @Override
    public int compareTo(Position pos) {
        Integer result = null;

        //First compare Rows
        if (this.row > pos.row) {
            result = 1;
        } else if (this.row < pos.row) {
            result = -1;
        }

        //If necessary compare Columns
        if (this.column > pos.column && result == null) {
            result = 1;
        } else if (this.column < pos.column && result == null) {
            result = -1;
        }

        return result != null ? result : 0;
    }
}
