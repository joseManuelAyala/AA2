package kastel.model;

/**
 * A class representing the result of a  Artificial player's move.
 * @author ucxug
 * @version 1.0
 */
public class MoveResult {
    /**
     * The Hexagon that must be placed on the current game board.
     */
    private final Hexagon movement;
    /**
     * Indicates whether the movements is a GameToken swap.
     */
    private final boolean swap;

    /**
     * Constructs a MoveResult with the specified Hexagon and swap indication.
     * @param hexagon the Hexagon representing the movement.
     * @param swap indicates if a swap is to be performed.
     */
    public MoveResult(final Hexagon hexagon, final boolean swap) {
        this.swap = swap;
        this.movement = hexagon;
    }

    /**
     * Gets the Hexagon represeting the movement.
     * @return the Hexagon representing the movement.
     */
    public Hexagon getMovement() {
        return this.movement;
    }

    /**
     * Checks if a swap is to be performed.
     * @return true if a swap is to be performed, false otherwise.
     */
    public boolean isSwap() {
        return this.swap;
    }

}
