 

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day02Solved extends DaySolution
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
    
    private String[][] guide;
    
    
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
        guide = new String[lines.length][];
        
        for (int i = 0; i < lines.length; i++) {
            guide[i] = lines[i].split(" ");
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
        
        int score = 0;
        
        for (String[] round : guide) {
            score += scoreRound(them(round[0]), me(round[1]));
        }
        
        return solution + score;
    }
    
    private String them(String choice) {
        if (choice.equalsIgnoreCase("A")) return "rock";
        if (choice.equalsIgnoreCase("B")) return "paper";
        /* if (choice.equalsIgnoreCase("C")) */ return "scissors"; 
    }
    
    private String me(String choice) {
        if (choice.equalsIgnoreCase("X")) return "rock";
        if (choice.equalsIgnoreCase("Y")) return "paper";
        /* if (choice.equalsIgnoreCase("Z")) */ return "scissors"; 
    }
    
    private int scoreRound(String them, String me) {
        int score = 0;
        if (me.equals("rock")) {
            score += 1; // rock
            switch (them) {
//                case "paper" -> score += 0; // lost
                case "rock" -> score += 3; // tie
                case "scissors" -> score += 6; // win
            }
        }
        if (me.equals("paper")) {
            score += 2; // rock
            switch (them) {
                case "paper" -> score += 3; // tie
                case "rock" -> score += 6; // win
//                case "scissors" -> score += 0; // lost
            }
        }
        if (me.equals("scissors")) {
            score += 3; // rock
            switch (them) {
                case "paper" -> score += 6; // win
//                case "rock" -> score += 0; // lost
                case "scissors" -> score += 3; // tie
            }
        }
        
        return score;
    }
    
    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        int score = 0;
        
        for (String[] round : guide) {
            String them = them(round[0]);
            score += scoreRound(them, myChoice(them, round[1]));
        }
        
        return "" + score;
    }
    
    public String myChoice(String them, String outcome) {
        String choice = "";
        
        if (them.equals("rock")) {
            switch (outcome.toUpperCase()) {
                case "X" -> choice = "scissors";
                case "Y" -> choice = them;
                case "Z" -> choice = "paper";
            }
        }
        if (them.equals("paper")) {
            choice = switch (outcome.toUpperCase()) {
                case "X" -> "rock";
                case "Y" -> them;
                case "Z" -> "scissors";
                default -> choice;
            };
        }
        if (them.equals("scissors")) {
            choice = switch (outcome.toUpperCase()) {
                case "X" -> "paper";
                case "Y" -> them;
                case "Z" -> "rock";
                default -> choice;
            };
        }
        
        return choice;
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
    }}
