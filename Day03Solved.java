import java.util.*;

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day03Solved extends DaySolution
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = ""; 
    // sample input files should be plain text files in the input/sample/
    // folder of this project. If a sample filename is provided, that file
    // that file's contents will be used when running the solution INSTEAD
    // OF your provided input.

    // Add any member variables you will need for the processed input or
    // to carry between question parts.
    private String[][] rucksacks;
    private final Map<String, Integer> priority = new HashMap<>();
    
    
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
        String[] lines = input.split("\n");
        rucksacks = new String[lines.length][2];

//        System.out.println(lines.length);

        for (int i = 0; i < rucksacks.length; i++) {
            rucksacks[i][0] = lines[i].substring(0, lines[i].length() / 2);
            rucksacks[i][1] = lines[i].substring(lines[i].length() / 2);
        }

        for (int i = 0; i < 26; i++) {
            String item = "" + (char)('a' + i);
            priority.put(item, i + 1);
//            System.out.println(item);
        }

        for (int i = 0; i < 26; i++) {
            String item = "" + (char)('A' + i);
            priority.put(item, i + 27);
//            System.out.println(item);
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

        int total = 0;

        for (String[] compartments : rucksacks) {
            for (String item : compartments[0].split("")) {
                if (item != null && compartments[1].contains(item)) {
//                    System.out.println(item);
                    total += priority.get(item);
                    break;
                }
            }
        }

        solution += total;
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

        int total = 0;
        int groupSize = 3;
        for (int group = 0; group < rucksacks.length / groupSize; group++) {
            Set<String> intersection = new HashSet<>();

            for (int i = 0; i < groupSize; i++) {
                Set<String> contents =new HashSet<>();

                for (String compartment : rucksacks[group * groupSize + i]) {
                    contents.addAll(Arrays.asList(compartment.split("")));
                }

                if (i == 0) {
                    intersection.addAll(contents);
                }
                else {
                    intersection.retainAll(contents);
                }
            }

            assert intersection.size() == 1;
            total += (intersection.stream().map(priority::get).reduce(Integer::sum)).orElse(0);
        }

        solution += total;
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
