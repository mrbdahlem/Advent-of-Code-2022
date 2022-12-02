 

/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day02Solved implements DaySolution
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = ""; 
    // sample input files should be plain text files in the input/sample/
    // folder of this project. If a sample filename is provided, that file
    // that file's contents will be used when running the solution INSTEAD
    // OF your provided input.
    
    // The provided (unprocessed) input
    private String input;
            
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
        this.input = Helper.loadInput(day, SAMPLE_INPUT_FILENAME);
        
        // process the input and initialise instance variables
        String[] lines = this.input.split("\n");
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
            if (them.equals("paper")) {
                score += 0; // lost 
            } 
            else if (them.equals("rock")) {
                score += 3; // tie
            }
            else if (them.equals("scissors")) {
                score += 6; // win
            }
        }
        if (me.equals("paper")) {
            score += 2; // rock
            if (them.equals("paper")) {
                score += 3; // tie
            } 
            else if (them.equals("rock")) {
                score += 6; // win
            }
            else if (them.equals("scissors")) {
                score += 0; // lost 
            }
        }
        if (me.equals("scissors")) {
            score += 3; // rock
            if (them.equals("paper")) {
                score += 6; // win
            } 
            else if (them.equals("rock")) {
                score += 0; // lost 
            }
            else if (them.equals("scissors")) {
                score += 3; // tie
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
                case "X":
                    choice = "scissors";
                    break;
                case "Y":
                    choice = them;
                    break;
                case "Z":
                    choice = "paper";
                    break;
            }
        }
        if (them.equals("paper")) {
            switch (outcome.toUpperCase()) {
                case "X":
                    choice = "rock";
                    break;
                case "Y":
                    choice = them;
                    break;
                case "Z":
                    choice = "scissors";
                    break;
            }
        }
        if (them.equals("scissors")) {
            switch (outcome.toUpperCase()) {
                case "X":
                    choice = "paper";
                    break;
                case "Y":
                    choice = them;
                    break;
                case "Z":
                    choice = "rock";
                    break;
            }
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
        
        // Clear the terminal
        System.out.print('\u000C');
        
        // Get a handle to this class (so that each day's solution follows
        // the same template
        Class thisClass = java.lang.invoke.MethodHandles.lookup().lookupClass();
        
        // Instantiate the solution from the class's handle. This is a weird
        // way to do this, so you shouldn't try to copy it.
        DaySolution todaysSolution = null;
        try {
            todaysSolution = (DaySolution)thisClass.newInstance();
        }
        catch (Exception e) {
            // If there is an exception constructing this class, exit with
            // an error message
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        // Load and parse the input for this day's problem
        todaysSolution.prepare();
        
        // Solve both parts of the problem
        String part1Solution = todaysSolution.part1();        
        String part2Solution = todaysSolution.part2();
        
        // Print out the solution for both parts. This happens at the end
        // so that answers can be copied/pasted even if debugging output is
        // provided
        System.out.println("Part 1 Solution: >" + part1Solution + "<");
        System.out.println("Part 2 Solution: >" + part2Solution + "<");
    }
}
