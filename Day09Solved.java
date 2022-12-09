import java.util.*;
/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day09Solved extends DaySolution
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = "";
//    public static String SAMPLE_INPUT_FILENAME = "sample.txt";
    // sample input files should be plain text files in the input/sample/
    // folder of this project. If a sample filename is provided, that file's
    // contents will be used when running the solution INSTEAD
    // OF your provided input.

    // Add any member variables you will need for the processed input or
    // to carry between question parts.
    private Command[] commands;

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

        commands = new Command[lines.length];
        for (int i = 0; i < lines.length; i++) {
            commands[i] = new Command(lines[i]);
        }
    }
    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        String solution = "(4295 low) >";

        // Solve part 1 for this day here

        Rope rope = new Rope(0,0, 2);
        for (Command c : commands) {
            rope.pull(c);
        }

        solution += rope.tailPos.size();
        return solution;
    }
        
    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        String solution = "";
        // Solve part 2 for this day here

        Rope rope = new Rope(0,0, 10);
        for (Command c : commands) {
            rope.pull(c);
        }

        solution += rope.tailPos.size();
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

    private static class Command {
        String dir;
        int dx = 0;
        int dy = 0;
        int dist;

        public Command(String c) {
            String[] parts = c.split(" ");
            dir = parts[0];

            switch (parts[0]) {
                case "U" -> dy = 1;
                case "D" -> dy = -1;
                case "R" -> dx = 1;
                case "L" -> dx = -1;
            }

            dist = Integer.parseInt(parts[1]);
        }

        public String toString() {
            return "(" + dx + "," + dy + ")" + dist;
        }
    }

    static class Rope{
        Point head;
        Point[] knots;
        Point tail;

        Set<Point> tailPos = new HashSet<>();

        public Rope(int headX, int headY, int size) {
            knots = new Point[size - 1];

            head = new Point(headX, headY);

            for (int i = 0; i < size - 1; i++) {
                knots[i] = new Point(headX, headY);
            }
            tail = knots[knots.length - 1];

            tailPos.add(new Point(tail.x, tail.y));
        }

        public void pull(Command c) {
            for (int i = 0; i < c.dist; i++) {
                head.x += c.dx;
                head.y += c.dy;

                Point last = head;
                for (Point p : knots) {
                    int dx = last.x - p.x;
                    int dy = last.y - p.y;

                    if ((Math.abs(dx) > 1) || (Math.abs(dy) > 1)) {
                        p.x += Math.signum(dx);
                        p.y += Math.signum(dy);

                    }

                    last = p;
                }

                tailPos.add(new Point(tail.x, tail.y));
//                System.out.println(c.dir + (i+1) + " H" + head + " T" + tail + " p" + tailPos.size());
            }
        }
    }

    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
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

        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
}
