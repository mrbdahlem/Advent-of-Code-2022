import java.util.*;
/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day12Solved extends DaySolution
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = "";
//    public static String SAMPLE_INPUT_FILENAME = "sample.txt";
    // sample input files should be plain text files in the
    // input/sample/dayXX folder of this project. If a sample filename
    // is provided, that file's contents will be used when running the
    // solution INSTEAD OF your provided input.

    // Add any member variables you will need for the processed input or
    // to carry between question parts.
    char[][] map; // the heightmap
    Point start = null; // the starting point
    Point end = null; // the ending point
    
    /**
     * Load the day's input then parse it for solving in part1 and part2.
     */
    public void prepare()
    {
        // Determine the day number from the name of the class
        int day = Integer.parseInt(this.getClass()
                                            .getSimpleName()
                                            .replaceAll("\\D", ""));
                                            
        System.out.println("\u000CAdvent of Code day " + day + " solution:");                                        
    
        // load or download the input file into the input String
        // The provided (unprocessed) input
        String input = Helper.loadInput(day, SAMPLE_INPUT_FILENAME);
        
        // process the input and initialise instance variables
        String[] lines = input.split("\n");
        map = new char[lines.length][lines[0].length()];

        // convert the input text into a 2D character array to use as a heightmap
        for (int row = 0; row < lines.length; row++) {
            map[row] = lines[row].toCharArray();
            for (int col = 0; col < map[row].length; col++) {
                // Check if the current location is the starting point
                if (map[row][col] == 'S') {
                    start = new Point(row, col);
                }
                // Check if the current location is the ending point
                else if (map[row][col] == 'E') {
                    end = new Point(row, col);
                }
            }
        }
    }

    /**
     * Print out the entire height map
     */
    public void printMap() {
        for (char[] row : map) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    /**
     * Print out the map of visited locations over top of the height map
     * @param visited a 2D boolean array where each cell is true if the location was visited
     */
    public void printMap(boolean[][] visited) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (visited[row][col]) {
                    System.out.print("*");
                }
                else {
                    System.out.print(map[row][col]);
                }
            }
            System.out.println();
        }
    }

    /**
     * Print out the heightmap overlaid with the moves taken to get to the end point
     * @param moves the list of moves from the starting point
     * @param start the starting point for the path
     */
    private void printMap(List<Move> moves, Point start) {
        char[][] moveMap = new char[map.length][map[0].length];

        for (int row = 0; row < moveMap.length; row++) {
            System.arraycopy(map[row], 0, moveMap[row], 0, moveMap[row].length);
        }

        Point p = start;
        for (Move m : moves) {
            moveMap[p.row][p.col] = switch (m) {
                case UP -> '^';
                case DOWN -> 'v';
                case LEFT -> '<';
                case RIGHT -> '>';
            };

            p = p.add(m);
        }

        for (char[] row : moveMap) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
//        printMap();

        String solution = "";
        // Solve part 1 for this day here

        List<Point> startPoints = new ArrayList<>();
        startPoints.add(start);
        List<Move> shortest = findPath(startPoints);

        if (shortest != null) {
//            printMap(shortest, start);
            solution += shortest.size();
        }
        return solution;
    }

    /**
     * Find the shortest path through the map from any of the startPoints to the ending point, never climbing more
     * than one level in the heightmap
     * @param startPoints a list of points that the shortest path could start at
     * @return the shortest possible list moves from one of the startPoints to the end point
     */
    private List<Move> findPath(List<Point> startPoints) {
        boolean[][] visited = new boolean[map.length][map[0].length];
        Deque<Path> paths = new ArrayDeque<>();
        for (Point p : startPoints) {
            paths.add(new Path(p, null, null));
        }

        while (!paths.isEmpty()) {
            Path soFar = paths.remove();

            Point current = soFar.next;

            // Don't use an already visited point
            if (visited[current.row][current.col]) {
                continue;
            }
            visited[current.row][current.col] = true;

            // get the moves taken to get to the current point
            List<Move> moves = soFar.moves;

            // Find the points possible to move to from the current point
            for (Move m : Move.values()) {
                Point next = current.add(m);

                // the next point must be on the map
                if (next.row < 0 || next.row >= map.length || next.col < 0 || next.col >= map[0].length) {
                    continue;
                }

                // Don't revisit any points
                if (visited[next.row][next.col]) {
                    continue;
                }

                // Find the current height and the height for the next point
                char height = map[current.row][current.col];
                char nextHeight = map[next.row][next.col];

                // If the next point is the end,
                if (nextHeight == 'E') {
                    // make sure that we can get there from here (only step up one level)
                    if (height == 'z') {
                        // If we can, this has to be the shortest path!
                        moves.add(m);
                        return moves;
                    }
                    continue;
                }

                // If the next point is not more than one level higher than the current point
                if ((height == 'S' && nextHeight == 'a') || (nextHeight <= height + 1)) {
                    // add the next point as a possible step along the path
                    Path nextPath = new Path(next, moves, m);
                    paths.add(nextPath);
                }
            }
        }

        // If we get here, there was no path found
//        printMap(visited);
        return null;
    }

        
    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        String solution = "";
        // Solve part 2 for this day here

        // Find the shortest path from ANY 'a' height location to the ending location
        List<Point> startPoints = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'a') {
                    startPoints.add(new Point(row, col));
                }
            }
        }
        List<Move> shortest = findPath(startPoints);

        if (shortest != null) {
//        printMap(shortest);
            solution += shortest.size();
        }
        return solution;
    }
    
    /**
     * A runner method for this day's solution.
     * In BlueJ, pass in a sample file name like {"sample.txt"} if the file
     * is stored in input/sample/dayXX/.
     */
    public static void main(String[] args) {
        if (args.length > 0 &&
                (SAMPLE_INPUT_FILENAME == null ||
                        SAMPLE_INPUT_FILENAME.isEmpty())) {
            SAMPLE_INPUT_FILENAME = args[0];
        }

        // Get a handle to this class (so that each day's solution follows
        // the same template
        Class<?> thisClass = java.lang.invoke.MethodHandles.lookup().lookupClass();
        DaySolution.runDay((Class<? extends DaySolution>) thisClass);
    }


    private static class Point {
        int row;
        int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Point add(Point other) {
            return new Point(this.row + other.row, this.col + other.col);
        }

        public Point add(Move m) {
            return new Point(this.row + m.dr, this.col + m.dc);
        }

        public String toString() {
            return "(" + row + "," + col + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (row != point.row) return false;
            return col == point.col;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }
    }

    private enum Move {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

        final int dr;
        final int dc;

        Move(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
        }
    }

    private static class Path {
        Point next;
        List<Move> moves;

        public Path(Point next, List<Move> soFar, Move toNext) {
            this.next = next;

            moves = new ArrayList<>();

            if (soFar != null) {
                moves.addAll(soFar);
            }

            if (toNext != null) {
                moves.add(toNext);
            }
        }
    }
}
