package kastel.model.searchalgorithms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import kastel.model.Game;
import kastel.model.GameTokens;
import kastel.model.Hexagon;

/**
 * A utility class that perforsm a Bread-Frist Search on a given board starting from a specified hexagon.
 * This class was made with help from the Wikipedia Link from the AA2 ÜB.
 * https://de.wikipedia.org/wiki/Breitensuche.
 * @author uxcug
 * @version 1.0
 */
public final class BreadthFirstSearch {
    private BreadthFirstSearch() {
        throw new UnsupportedOperationException("Utility class can not be initialized");
    }

    /**
     * Performs BFS on the Board.
     * @param currentGame    the current game that is being played..
     * @param startHexagon the starting hexagon for the path.
     * @param endHexagon the ending hexagon for the path.
     * @param targetToken  the Target GameToken to search for in the board.
     * @return a list of hexagons if a path is found, an empty list otherwise.
     */
    public static List<Hexagon> breadthFirstSearch(final Game currentGame,
                                                   final Hexagon startHexagon,
                                                   final Hexagon endHexagon,
                                                   final GameTokens targetToken) {
        Queue<Hexagon> hexagonsToExplore = new LinkedList<>();
        Map<Hexagon, Hexagon> winningPath = new HashMap<>();
        List<Hexagon> winningHexagons =  new LinkedList<>(); //Stores the hexagons forming the winning path.
        boolean containsEndHexagon = false;
        //Indicates wheter the end hexagon is parth of the path.
        hexagonsToExplore.offer(startHexagon);

        while (!hexagonsToExplore.isEmpty()) {
            Hexagon currentHexagon = hexagonsToExplore.poll();
            for (Hexagon hexagonNeighbor : currentGame.getGameBoard().getHexagonNeighbours(currentHexagon)) {
                boolean isNewHexagon = !winningPath.containsKey(hexagonNeighbor);
                //The neighbor hexagon wont be added if it is already visited.
                if (isNewHexagon && hexagonNeighbor.getContent() == targetToken) {
                    //The visited neihghbor content must match with the given target token.
                    if (hexagonNeighbor == endHexagon) {
                        containsEndHexagon = true;
                    }
                    hexagonsToExplore.offer(hexagonNeighbor);
                    winningPath.put(hexagonNeighbor, currentHexagon);
                }
            }
        }
        if (!containsEndHexagon) {
            //If the ending Hexagon is not in the path an empty map will be returned.
            return new LinkedList<>();
        }
        Hexagon currentHexagon = endHexagon;
        while (!winningHexagons.contains(currentHexagon)) {
            //Builds the winning path in the correct order.
            winningHexagons.add(currentHexagon);
            currentHexagon = winningPath.get(currentHexagon);
        }
        return winningHexagons;
    }

}
