/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day08Solved extends DaySolution
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
    Tree[][] forest;
    
    
    
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
        forest = new Tree[lines.length][lines[0].length()];

        for (int i = 0; i < lines.length; i++) {
            char[] row = lines[i].toCharArray();
            for (int j = 0; j < row.length; j++) {
                forest[i][j] = new Tree(row[j] - '0');
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

        // Determine how many trees in the forest are visible from outside the forest
        for (int row = 0; row < forest.length; row++) {
            for (int col = 0; col < forest[row].length; col++) {
                if (row == 0 || col == 0 || row == forest.length - 1 || col == forest[row].length - 1) {
                    forest[row][col].visible = true;
                    continue;
                }

                checkVisible(row, col);
            }
        }

        int count = 0;
        for (Tree[] trees : forest) {
            for (Tree tree : trees) {
                if (tree.visible) {
                    count++;
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        solution += count;
        return solution;
    }

    private void checkVisible(int row, int col) {
        Tree tree = forest[row][col];

        if (visibleFromNorth(row,col,tree.height) || visibleFromSouth(row,col,tree.height) ||
            visibleFromWest(row,col,tree.height) || visibleFromEast(row,col,tree.height)) {
            tree.visible = true;
        }
    }

    private boolean visibleFromNorth(int row, int col, int height) {
        for (row--; row >= 0; row--) {
            if (forest[row][col].height >= height) {
                return false;
            }
        }
        return true;
    }
    private boolean visibleFromSouth(int row, int col, int height) {
        for (row++; row < forest.length; row++) {
            if (forest[row][col].height >= height) {
                return false;
            }
        }
        return true;
    }

    private boolean visibleFromWest(int row, int col, int height) {
        for (col--; col >= 0; col--) {
            if (forest[row][col].height >= height) {
                return false;
            }
        }
        return true;
    }

    private boolean visibleFromEast(int row, int col, int height) {
        for (col++; col < forest[row].length; col++) {
            if (forest[row][col].height >= height) {
                return false;
            }
        }
        return true;
    }

    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        String solution = "";
        // Solve part 2 for this day here

        int maxScore = 0;
        for (int row = 0; row < forest.length; row++) {
            for (int col = 0; col < forest[row].length; col++) {
                int score = scenicScore(row, col);
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }
        
        solution += maxScore;
        return solution;
    }

    private int scenicScore(int row, int col) {
        Tree tree = forest[row][col];

        int score = scoreViewNorth(row, col, tree.height);
        score *= scoreViewSouth(row, col, tree.height);
        score *= scoreViewWest(row, col, tree.height);
        score *= scoreViewEast(row, col, tree.height);

        return score;
    }

    private int scoreViewNorth(int row, int col, int height) {
        int score = 0;
        for (row--; row >= 0; row--) {
            score++;
            if (forest[row][col].height >= height) {
                break;
            }
        }
        return score;
    }
    private int scoreViewSouth(int row, int col, int height) {
        int score = 0;
        for (row++; row < forest.length; row++) {
            score++;
            if (forest[row][col].height >= height) {
                break;
            }
        }
        return score;
    }

    private int scoreViewWest(int row, int col, int height) {
        int score = 0;
        for (col--; col >= 0; col--) {
            score++;
            if (forest[row][col].height >= height) {
                break;
            }
        }
        return score;
    }

    private int scoreViewEast(int row, int col, int height) {
        int score = 0;
        for (col++; col < forest[row].length; col++) {
            score++;
            if (forest[row][col].height >= height) {
                break;
            }
        }
        return score;
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

    private static class Tree {
        int height;
        boolean visible;
        boolean visited;

        public Tree(int height) {
            this.height = height;
            this.visible = false;
            this.visited = false;
        }
    }
}
