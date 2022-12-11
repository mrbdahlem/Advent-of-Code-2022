import java.util.*;
/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day07Solved extends DaySolution
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
    private Directory root;
    private final List<Directory> directories = new ArrayList<>();
    
    
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
        String[] lines = input.split("\n");

        // process the input and initialise instance variables
        root = new Directory("/", null);

        Directory current = root;

        for (String line : lines) {
            String[] parts = line.split(" ");

            // IF this is a command
            if (parts[0].equals("$")) {
                // if it is a change dir command
                if (parts[1].equals("cd")) {
                    // ".." moves back to parent dir
                    if (parts[2].equals("..")) {
                       current = current.parent;
                       if (current == null) {
                           throw new RuntimeException("Left dir structure");
                       }
                    }
                    // / moves to root
                    else if (parts[2].equals("/")) {
                        current = root;
                    }
                    // anything else must be a subdir
                    else {
                        current = current.getSubDir(parts[2]);
                    }
                }
            }
            // If the output starts with dir, it is part of a dir listing and is a subdir
            else if (parts[0].equals("dir")) {
                Directory sub = new Directory(parts[1], current);
                current.add(sub);
                directories.add(sub);
            }
            // anything else must be part of a dir listing and must be a file
            else {
                int size = Integer.parseInt(parts[0]);
                String name = parts[1];
                File f = new File(size, name);
                current.add(f);
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
        // Find all directories whose contents are <= 100,000 in size
        long total = 0;
        for (Directory dir : directories) {
            if (dir.name.equals("/")) {
                continue;
            }
            int size = dir.size();
            if (size <= 100000) {
                total += size;
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

        // We need at least 30,000,000 free space to update
        int rootSize = root.size();
        int spaceNeeded = 30000000 - (70000000 - rootSize);

        // Find the smallest dir that will give us enough space to update
        int smallestSize = rootSize;
        for (Directory dir : directories) {
            int size = dir.size();
            if (size >= spaceNeeded && size < smallestSize) {
                smallestSize = size;
            }
        }

        solution += smallestSize;
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

    private static class Directory {
        List<Directory> subDirs = new ArrayList<>();
        List<File> files = new ArrayList<>();
        String name;
        Directory parent;

        public Directory(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
        }

        public void add(Directory d) {
            subDirs.add(d);
        }

        public void add(File f) {
            files.add(f);
        }

        public int size() {
            int total = 0;
            for (Directory dir : subDirs) {
                total += dir.size();
            }

            for (File f : files) {
                total += f.size;
            }

            return total;
        }

        public Directory getSubDir(String name) {
            for (Directory sub : subDirs) {
                if (sub.name.equals(name)) {
                    return sub;
                }
            }

            throw new RuntimeException("Subdir not found in " + this.name + ": " + name );
        }
    }

    private static class File {
        int size;
        String name;

        public File(int size, String name) {
            this.size = size;
            this.name = name;
        }
    }
}
