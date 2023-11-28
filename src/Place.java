/**
 * An intersection on an Omok board identified by its 0-based column
 * index (x) and row index (y). The indices determine the position
 * of a place on the board, with (0, 0) denoting the top-left
 * corner and (n-1, n-1) denoting the bottom-right corner,
 * where n is the size of the board.
 */
public class Place {
    /** 0-based column index of this place. */
    public final int x;

    /** 0-based row index of this place. */
    public final int y;

    /** Create a new place of the given indices.
     *
     * @param x 0-based column (vertical) index
     * @param y 0-based row (horizontal) index
     */
    public Place(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**Returns int x from the Object Place
     *
     * @return Returns int x found in Place
     */
    public int getX(){
        return x;
    }

    /**Returns y from the Object Place
     *
     * @return Returns int y found in Place
     */
    public int getY() {
        return y;
    }
}
