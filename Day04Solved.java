import java.util.ArrayList;
import java.util.List;

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day04Solved extends DaySolution
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
    private final List<ElfPair> pairs = new ArrayList<>();
    
    /**
     * Load the day's input then parse it for solving in part1 and part2.
     */
    public void prepare()
    {
        // Determine the day number from the name of the class
        int day = Integer.parseInt(this.getClass()
                                            .getSimpleName()
                                            .replaceAll("\\D", ""));
                                            
        System.out.println("Advent of Code day " + day + " solution:");                                        
    
        // load or download the input file into the input String
        // The provided (unprocessed) input
        String input = Helper.loadInput(day, SAMPLE_INPUT_FILENAME);
        
        // process the input and initialise instance variables
        String[] pairString = input.split("\n");
        for (String s : pairString) {
            pairs.add(new ElfPair(s));
        }
        
    }

    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        String solution = "(601 high) >";
        // Solve part 1 for this day here

        int count = 0;
        for (ElfPair pair : pairs) {
            if (pair.fullyContains()) count++;
        }

        solution += count;
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


        int count = 0;
        for (ElfPair pair : pairs) {
            if (pair.hasOverlap()) count++;
        }

        solution += count;
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

    private static class ElfPair {
        Elf a;
        Elf b;

        public ElfPair(String s) {
            String[] ranges = s.split(",");
            a = new Elf(ranges[0]);
            b = new Elf(ranges[1]);
        }

        public boolean fullyContains() {
            if (a.minVal >= b.minVal && a.maxVal <= b.maxVal) {
                return true;
            }
            return b.minVal >= a.minVal && b.maxVal <= a.maxVal;
        }

        public boolean hasOverlap() {
            if (fullyContains()) {
                return true;
            }
            if (a.minVal <= b.minVal && a.maxVal >= b.minVal) {
                return true;
            }

            if (b.minVal <= a.minVal && b.maxVal >= a.maxVal) {
                return true;
            }

            return  (a.minVal <= b.maxVal && a.maxVal >= b.maxVal);
        }
    }

    private static class Elf {
        int minVal;
        int maxVal;

        public Elf(String s) {
            String[] vals = s.split("-");
            minVal = Integer.parseInt(vals[0]);
            maxVal = Integer.parseInt(vals[1]);

        }
    }
}
