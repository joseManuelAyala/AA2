package kastel.model;

import java.util.Objects;

/**
 * A class representing a Hexagon in the game board.
 * @author ucxug
 * @version 1.0
 */

public class Hexagon {

    /**
     * The x coordinate of the Hexagon on the game board.
     */
    private final int xCoordinate;
    /**
     * The y coordinate of the Hexagon on the game board.
     */
    private final int yCoordinate;
    /**
     * The GameToken present on the Hexagon.
     */
    private GameTokens content;

    /**
     * Constructs a Hexagon with the specified coordinates.
     * @param xCoordinate the x coordinate of the Hexagon.
     * @param yCoordinate the y coordinate of the Hexagon.
     */
    public Hexagon(final int xCoordinate, final int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.content = GameTokens.EMPTY;
    }

    /**
     * Constructs a copy of this Hexagon.
     * @param currentHexagon the Hexagon to be copied.
     */
    private Hexagon(final Hexagon currentHexagon) {
        this.xCoordinate = currentHexagon.xCoordinate;
        this.yCoordinate = currentHexagon.yCoordinate;
        this.content = currentHexagon.content;
    }

    /**
     * Sets the content of the new specified content.
     * @param newContent the new content to set for the Hexagon.
     */
    public void setContent(final GameTokens newContent) {
        this.content = newContent;
    }

    /**
     * Gets the content of the Hexagon.
     * @return the content of the Hexagon.
     */
    public GameTokens getContent() {
        return this.content;
    }

    /**
     * Gets the x coordinate of the Hexagon.
     * @return the x coordinate of the Hexagon.
     */
    public int getxCoordinate() {
        return this.xCoordinate;
    }

    /**
     * Gets the y coordinate of the Hexagon.
     * @return the y coordinate of the Hexagon.
     */
    public int getyCoordinate() {
        return this.yCoordinate;
    }

    /**
     * Returns a copy of the hexagon.
     * @return the copy of the hexagon.
     */
    public Hexagon getCopy() {
        return new Hexagon(this);
    }


    /**
     * Computes the hash code for the Hexagon based on its coordinates and content.
     * @return the computed hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.xCoordinate, this.yCoordinate);
    }



    /**
     * Checks if this Hexagon is equal to another object.
     * Two Hexagons are considered equal if they have the same coordinates and content.
     * @param obj the object to compare to this Hexagon.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Hexagon hexagon = (Hexagon) obj;
        return xCoordinate == hexagon.getxCoordinate()
            && yCoordinate == hexagon.getyCoordinate();
    }

}
