import java.util.ArrayList;
import java.util.List;

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day05Solved extends DaySolution
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = ""; 
    // sample input files should be plain text files in the input/sample/
    // folder of this project. If a sample filename is provided, that
    // file's contents will be used when running the solution INSTEAD
    // OF your provided input.

    // Add any member variables you will need for the processed input or
    // to carry between question parts.
    private String[] commands;

    private List<String>[] cargo1;
    private List<String>[] cargo2;


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
        String[] parts = input.split("\n\n");
        commands = parts[1].split("\n");

        String[] lines = parts[0].split("\n");
        int maxlen = 0;
        for (String line : lines) {
            if (line.length() > maxlen) maxlen = line.length();
        }

        int cols = lines[lines.length - 1].split("\\w+").length - 1;

        cargo1 = new List[cols];
        cargo2 = new List[cols];

        for (int i = 0; i < cols; i++) {
            cargo1[i] = new ArrayList<>();
            cargo2[i] = new ArrayList<>();
        }

        for (String line : lines) {
            int pos = line.indexOf("[");
            while (pos > -1) {
                int col = pos / 4;
                String item = line.substring(pos + 1, pos + 2);
                cargo1[col].add(item);
                cargo2[col].add(item);

                pos = line.indexOf("[", pos + 1);
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


//        for (List<String> stack : cargo) {
//            solution += stack.get(0);
//        }
//
        for (String command : commands) {
            String[] cParts = command.split(" ");
            int qty = Integer.parseInt(cParts[1]);
            int from = Integer.parseInt(cParts[3]) - 1;
            int to = Integer.parseInt(cParts[5]) - 1;

            for (int i = 0; i < qty; i++) {
                String item = cargo1[from].remove(0);
                cargo1[to].add(0, item);
            }
        }

        for (List<String> stack : cargo1) {
            solution += stack.get(0);
        }

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
        for (String command : commands) {
            String[] cParts = command.split(" ");
            int qty = Integer.parseInt(cParts[1]);
            int from = Integer.parseInt(cParts[3]) - 1;
            int to = Integer.parseInt(cParts[5]) - 1;

            List<String> items = new ArrayList<>();
            for (int i = 0; i < qty; i++) {
                String item = cargo2[from].remove(0);
                items.add(item);
            }

            cargo2[to].addAll(0, items);
        }

        for (List<String> stack : cargo2) {
            solution += stack.get(0);
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
}
