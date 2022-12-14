import java.util.*;
/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day14Solved extends DaySolution
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
    Map<Point, Thing> cave = new HashMap<>();
    Map<Point, Thing> cave2;

    int minX, maxX, minY, maxY;

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

        maxX = Integer.MIN_VALUE;
        minX = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;
        minY = Integer.MAX_VALUE;

        // Parse each line of input as a description of lines
        for (String line : lines) {
            // each line is shown as a path through endpoints
            Point[] points = Arrays.stream(line.split(" -> "))
                    .map(Point::new)
                    .toArray(Point[]::new);

            // Loop through all the points in the line
            Point last = points[0];
            for (Point p : points) {
                int dx = last.x - p.x;
                int dy = last.y - p.y;

                // Determine if the line segment is vertical or horizontal
                if (Math.abs(dx) > Math.abs(dy)) {
                    // Draw a horizontal line segment
                    for (int x = p.x; x != last.x; x += Math.signum(dx)) {
                        cave.put(new Point(x, p.y), Thing.ROCK);
                    }
                    cave.put(new Point(last.x, last.y), Thing.ROCK);
                }
                else {
                    // Draw a vertical line segment
                    for (int y = p.y; y != last.y; y += Math.signum(dy)) {
                        cave.put(new Point(p.x, y), Thing.ROCK);
                    }
                    cave.put(new Point(last.x, last.y), Thing.ROCK);
                }

                // Find the extents of all segments drawn
                if (p.x < minX) {
                    minX = p.x;
                }
                if (p.x > maxX) {
                    maxX = p.x;
                }
                if (p.y < minY) {
                    minY = p.y;
                }
                if (p.y > maxY) {
                    maxY = p.y;
                }

                last = p;
            }
        }
//        System.out.println(minX + "," + minY + "  " + maxX + "," + maxY);
        cave2 = new HashMap<>(cave);
    }

    /**
     * Draw the entire cave
     * @param cave the cave to draw
     */
    private void showCave(Map<Point, Thing> cave) {
        for (int y = 0; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Thing item = cave.get(new Point(x, y));

                if (y == 0 && x == 500) {
                    System.out.print("X");
                }
                else if (item == null) {
                    System.out.print(" ");
                }
                else {
                    switch (item) {
                        case ROCK -> System.out.print("#");
                        case SAND -> System.out.print("o");
                    }
                }
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
        String solution = "";

        int grains = 0;
        boolean done = false;

        // Drop grains until they fall past the last obstacle, counting each one
        while (!done) {
            grains++;
            done = dropGrain(false);
        }

        solution += (grains - 1);
        return solution;
    }

    final static Point start = new Point(500, 0);
    final static Point delta = new Point(0, 1);
    final static Point left = new Point(-1, 1);
    final static Point right = new Point(1, 1);
    private boolean dropGrain(boolean floor) {
        Point grain = start;

        boolean moving = true;
        boolean done = false;
        while (moving && !done) {
            // Try to fall straight down
            Point next = grain.add(delta);

            // If there is something directly below
            if (cave.get(next) != null) {
                // Try rolling left
                next = grain.add(left);
            }

            // If there is something blocking the left
            if (cave.get(next) != null) {
                // Try rolling right
                next = grain.add(right);
            }

            // If there is something blocking the right
            if (cave.get(next) != null) {
                // This grain is done moving
                moving = false;
                cave.put(grain, Thing.SAND); // Lock the grain in place
            }

            if (moving) {
                grain = next;
            }

            // If there is no floor
            if (!floor) {
                // If the grain has moved past the last obstacle
                if (grain.y > maxY) {
                    done = true; // We are done
                }
            }
            // If there is a floor
            else {
                // If the grain has reached the floor
                if (grain.y == maxY + 1) {
                    // the grain should stop where it is
                    moving = false;
                    cave.put(grain, Thing.SAND);
                }

                // If the grain is still at the entry point, we must have filled the cave
                if (grain.equals(start)) {
                    done = true;
                }
            }
        }

        return done;
    }
        
    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        String solution = "";
        // Solve part 2 for this day here

        // Switch to an empty copy of the cave
        this.cave = cave2;

        int grains = 0;
        boolean done = false;
        // Drop grains until they fill the cave up to the source
        while (!done) {
            grains++;
            done = dropGrain(true);
        }

//        showCave(cave2);

        solution += grains;
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

    public static class Point {
        final int x;
        final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(String s) {
            String[] coords = s.split(",");
            this.x = Integer.parseInt(coords[0]);
            this.y = Integer.parseInt(coords[1]);
        }

        public Point add(Point o) {
            return new Point(this.x + o.x, this.y + o.y);
        }
        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    enum Thing {
        SAND, ROCK
    }
}
