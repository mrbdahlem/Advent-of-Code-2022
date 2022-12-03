import java.util.*; 

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day01Solved extends DaySolution
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = ""; 
    // sample input files should be plain text files in the input/sample/
    // folder of this project. If a sample filename is provided, that file
    // that file's contents will be used when running the solution INSTEAD
    // OF your provided input.

    // Add any member variables you will need for the processed input or
    // to carry between question parts.
    private int[][] elfCalories;
    private int[] elfTotal;
    
    
    
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
        String[] elves = input.split("\n\n");
        elfCalories = new int[elves.length][];
        elfTotal = new int[elves.length];
        
        for (int i = 0; i < elves.length; i++) {
            String[] cals = elves[i].split("\n");
            elfCalories[i] = new int[cals.length];
            for (int j = 0; j < cals.length; j++) {
                elfCalories[i][j] = Integer.parseInt(cals[j]);
            }
        }
        
    }

    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        String solution = "";
        // Solve part 1 for this day here
        int maxCals = 0;
        for (int i = 0 ; i < elfCalories.length; i++) {
            for (int cals : elfCalories[i]) {
                elfTotal[i] += cals;
            }
            if (elfTotal[i] > maxCals) {
                maxCals = elfTotal[i];
            }
        }
        
        solution += maxCals;
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
     
        int top3 = 0;
        Arrays.sort(elfTotal);
        for (int i = elfTotal.length - 1; i > elfTotal.length - 4; i--) {
            top3 += elfTotal[i];
        }
        
        solution += top3;
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
}
