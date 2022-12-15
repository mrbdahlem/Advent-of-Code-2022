import java.util.*;

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day15Solved extends DaySolution
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
    private final Set<Sensor> sensors = new HashSet<>();
    private final Set<Point> beacons = new HashSet<>();

    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;


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

        for (String line : lines) {
            String[] parts = line.split(":");

            Point sensorLoc = new Point(parts[0].substring(parts[0].indexOf("x")));
            Point beaconLoc = new Point(parts[1].substring(parts[1].indexOf("x")));

            sensors.add(new Sensor(sensorLoc, beaconLoc));
            beacons.add(beaconLoc);

            if (sensorLoc.x < minX) {
                minX = sensorLoc.x;
            }
            if (beaconLoc.x < minX) {
                minX = beaconLoc.x;
            }
            if (sensorLoc.y < minY) {
                minY = sensorLoc.y;
            }
            if (beaconLoc.y < minY) {
                minY = beaconLoc.y;
            }
            if (sensorLoc.x > maxX) {
                maxX = sensorLoc.x;
            }
            if (beaconLoc.x > maxX) {
                maxX = beaconLoc.x;
            }
            if (sensorLoc.y > maxY) {
                maxY = sensorLoc.y;
            }
            if (beaconLoc.x > maxX) {
                maxY = beaconLoc.y;
            }
        }
//        System.out.println(sensors.size() + new Point(minX, minY).toString() + new Point(maxX, maxY).toString());
    }
    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        String solution = "";
        // Solve part 1 for this day here

        final int ROW = 2_000_000;
//        final int ROW = 10;

        // Find the ranges of points on the row where there cannot be a beacon
        List<Range> ranges = excluded(ROW);

        // Don't exclude the points where there is a beacon
        for (Point b : beacons) {
            // If the beacon isn't on this row, ignore it
            if (b.y != ROW) {
                continue;
            }

            // Search through each range of excluded points
            ListIterator<Range> ri = ranges.listIterator();
            while (ri.hasNext()) {
                Range r = ri.next();

                // If the beacon is within this range
                if (r.contains(b.x)) {
                    // remove the range from the list
                    ri.remove();

                    // and add a range to the left and to the right of the beacon
                    if (r.low < b.x - 1) {
                        Range left = new Range(r.low, b.x - 1);
                        ri.add(left);
                    }
                    if (r.high > b.x + 1) {
                        Range right = new Range(b.x + 1, r.high);
                        ri.add(right);
                    }
                }
            }
        }

        // Calculate how many points cannot possibly contain a beacon on the given row
        long excluded = 0;
        for (Range r : ranges) {
            excluded += r.contains;
        }

        solution += excluded;
        return solution;
    }

    /**
     * Find the range(s) of cells excluded from containing a beacon in a given row on the map
     * @param row the row to search for excluded cells
     * @return the range(s) of cells that cannot contain a beacon on that row
     */
    private List<Range> excluded(int row) {
        List<Range> ranges = new ArrayList<>();

        for (Sensor sensor : sensors) {
            int vDist = Math.abs(row - sensor.location.y);
            if (vDist <= sensor.distance) {
                int xOffset = (sensor.distance - vDist);

                ranges.add(new Range(sensor.location.x - xOffset, sensor.location.x + xOffset));
            }
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = ranges.size() - 1; i >= 0; i--) {
                Range ri = ranges.get(i);
                for (int j = i - 1; j >= 0; j--) {
                    Range rj = ranges.get(j);

                    if (ri.overlaps(rj)) {
                        ri = ri.add(rj);
                        ranges.set(i, ri);
                        ranges.remove(j);
                        i--;
                        j--;
                        changed = true;
                    }
                }
            }
        }

        return ranges;
    }

    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        String solution = "(1461548630 low) >";
        // Solve part 2 for this day here

        final int MAX = 4_000_000;
//        final int MAX = 20; // for the sample

        int x = -1;
        int y = -1;

        // Check each row in the possible area
        for (int row = 0; row <= MAX; row++) {
            List<Range> excluded = excluded(row);

            // Find out how many cells in the area are excluded from containing the beacon
            int count = 0;
            ListIterator<Range> iterator = excluded.listIterator();
            while (iterator.hasNext()) {
                // Make sure to only count excluded cells within the bounds of the area
                Range r = iterator.next();
                if (r.low < 0) {
                    r = new Range(0, r.high);
                    iterator.set(r);
                }
                if (r.high > MAX) {
                    r = new Range(r.low, MAX);
                    iterator.set(r);
                }
                count += r.contains;
            }

            // If we find an empty cell
            if (count < MAX + 1) {
                // Determine the coordinates of that cell
                int first = excluded.get(0).high + 1;
                int second = excluded.get(1).high + 1;
                x = Math.min(first, second);
                y = row;
            }
        }

        // Figure out the tuning frequency to isolate the beacon at the cell
        long tuning = ((long)x) * MAX + y;
        solution += tuning;
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
            this.x = Integer.parseInt(coords[0].substring(coords[0].indexOf("=") + 1));
            this.y = Integer.parseInt(coords[1].substring(coords[1].indexOf("=") + 1));
        }

        public Point add(Point o) {
            return new Point(this.x + o.x, this.y + o.y);
        }

        public int manhattanDistance(Point other) {
            int dx = Math.abs(this.x - other.x);
            int dy = Math.abs(this.y - other.y);

            return dx + dy;
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

    public static class Range {
        final int low;
        final int high;
        final int contains;
        public Range(int low, int high) {
            this.low = low;
            this.high = high;
            this.contains = (high - low) + 1;
        }

        /**
         * Combine this range and another overlapping one into a single range - ranges MUST overlap
         * @param other the other range to combine with this one
         * @return the combined range
         */
        public Range add(Range other) {
            if (other.low <= this.low && other.high >= this.low) {
                return new Range(other.low, Math.max(this.high, other.high));
            }
            else if (this.low <= other.low && this.high >= other.low) {
                return new Range(this.low, Math.max(other.high, this.high));
            }
            throw new RuntimeException("Non-overlapping ranges! " + this + ", " + other);
        }

        /**
         * Determine if this range overlaps another
         * @param other the range check against this range
         * @return true if the ranges overlap
         */
        public boolean overlaps(Range other) {
            if (other.low <= this.low && other.high >= this.low) {
                return true;
            }
            return (this.low <= other.low && this.high >= other.low);
        }

        /**
         * Determine if a value falls within this range
         * @param val the value to check against this range
         * @return true if the value is within this range
         */
        public boolean contains(int val) {
            return this.low <= val && val <= this.high;
        }

        @Override
        public String toString() {
            return "(" + low + "->" + high + "):" + contains;
        }
    }

    static class Sensor {
        final Point location;
        final Point beacon;

        final int distance;

        public Sensor(Point location, Point beaconLocation) {
            this.location = location;
            this.beacon = beaconLocation;

            distance = location.manhattanDistance(beaconLocation);
        }
    }
}
