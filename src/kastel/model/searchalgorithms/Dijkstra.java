package kastel.model.searchalgorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import kastel.model.Hexagon;
import kastel.model.GameBoard;
import kastel.model.GameTokens;

/**
 * A utility class that implements Dijkstra's algortihm for finding a shortest path in the game board.
 * This class was made with help from the Link in the AA2 ÜB.
 * https://de.wikipedia.org/wiki/Dijkstra-Algorithmus
 * @author ucxug
 * @version 1.0
 */
public final class Dijkstra {

    private static final int DEFAULT_DISTANCE_BETWEEN_HEXAGONS = 1;
    private Dijkstra() {
        throw new UnsupportedOperationException("Utility class can not be initialized!");
    }

    /**
     * Applies Dijkstra's algorithm to find the shortest distances from a specified start hexagon to all other
     * hexagon in the board.
     * @param gameBoard the game board to apply the Dijkstra's algortihm on.
     * @param endHexagon the starting hexagon for the algorithm.
     * @param startHexagon the starting hexagon for the algorithm.
     * @param token the target GameToken used for traversal conditions.
     * @return a map of hexagons and their corresponding predecessor hexagons on the shortes path.
     */
    public static Map<Hexagon, Hexagon> dijkstraWithPredecessors(final GameBoard gameBoard,
                                                                 final Hexagon endHexagon,
                                                                 final Hexagon startHexagon, final GameTokens token) {
        Map<Hexagon, Integer> hexagonDistances = new HashMap<>(); //Stores the distances between the hexagons.
        Map<Hexagon, Hexagon> shortestPath = new HashMap<>();
        List<Hexagon> visitedHexagons = new LinkedList<>();
        PriorityQueue<Hexagon> queue = new PriorityQueue<>(Comparator.comparingInt(hexagonDistances::get));
        boolean containsEndHexagon = false; //Indicates whether the end hexagon is part of the winnin path.
        hexagonDistances.put(startHexagon, 0); //The start distance is set to 0.
        queue.add(startHexagon);

        while (!queue.isEmpty()) {
            Hexagon currentHexagon = queue.poll();
            visitedHexagons.add(currentHexagon);
            //Add the hexagon to the visited hexagons list.
            for (Hexagon neighbor : gameBoard.getHexagonNeighbours(currentHexagon)) {
                int newDistance = hexagonDistances.get(currentHexagon) + DEFAULT_DISTANCE_BETWEEN_HEXAGONS;
                boolean isNewHexagon = !visitedHexagons.contains(neighbor);
                if (isNewHexagon && neighbor.getContent() == token
                    && (!hexagonDistances.containsKey(neighbor) || newDistance < hexagonDistances.get(neighbor))) {
                    //The hexagon content must match the given content and the distance be
                    // less that the other neighbors.
                    hexagonDistances.put(neighbor, newDistance);
                    shortestPath.put(neighbor, currentHexagon);
                    queue.add(neighbor);
                    if (neighbor == endHexagon) {
                        //If the neighbors is the end hexagon, will be indicated that the end hexagon is part
                        // of the path.
                        containsEndHexagon = true;
                    }
                }
            }
        }

        if (!containsEndHexagon) {
            //If the end hexagon is not part of the path an empty map will be returned.
            return new HashMap<>();
        }
        return shortestPath;
    }
}
