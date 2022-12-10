/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day10Solved extends DaySolution
{
    // Adjust these to test against the proper input file(s)
    public static String SAMPLE_INPUT_FILENAME = "";
    // sample input files should be plain text files in the input/sample/
    // folder of this project. If a sample filename is provided, that
    // file's contents will be used when running the solution INSTEAD
    // OF your provided input.

    // Add any member variables you will need for the processed input or
    // to carry between question parts.
    private String[] program;

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
        program = input.split("\n");
    }

    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        String solution = "(116 low) >";
        // Solve part 1 for this day here
        Cpu cpu = new Cpu(program);

        boolean done = false;
        while (!done) {
            done = cpu.clockPulse();
        }

        solution += cpu.getSignalStrength();
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
        Cpu cpu = new Cpu(program);
        CRT crt = new CRT();

        boolean done = false;
        while (!done) {
            crt.next(cpu.x);
            done = cpu.clockPulse();
        }

        solution += crt.display();
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


    private static class Cpu {
        int x;
        String[] program;
        int programCounter;

        Instruction currentInstruction;
        int instructionCycles;
        int cycleNum = 0;
        int signalStrengthSum = 0;

        public Cpu(String[] program) {
            this.x = 1;
            this.currentInstruction = null;
            this.program = program;
        }

        public boolean clockPulse() {
            if (currentInstruction == null) {
                currentInstruction = Instruction.decode(program[programCounter]);
                programCounter++;
                if (currentInstruction != null) {
                    this.instructionCycles = currentInstruction.numCycles();
                }
            }

            cycleNum++;

            if ((cycleNum - 20) % 40 == 0) {
                int ss = (cycleNum * x);
                signalStrengthSum += ss;
            }

            instructionCycles--;
            if (instructionCycles <= 0) {
                if (currentInstruction != null && currentInstruction.op == Instruction.OpCode.ADD_X) {
                    x += currentInstruction.val;
                }
                currentInstruction = null;
            }

            return (programCounter >= program.length) && (instructionCycles == 0);
        }

        public int getSignalStrength() {
            return signalStrengthSum;
        }

        private static class Instruction {
            public enum OpCode {
                NOP(1), ADD_X(2);

                public final int cycles;

                OpCode(int cycles) {
                    this.cycles = cycles;
                }

            }

            private final int val;
            private final OpCode op;

            public static Instruction decode(String instruction) {
                String[] parts = instruction.split(" ");
                return switch (parts[0].toLowerCase()) {
                    case "addx" -> new Instruction(OpCode.ADD_X, Integer.parseInt(parts[1]));
                    default /* "noop" */ -> new Instruction(OpCode.NOP);
                };
            }

            public int numCycles() {
                return this.op.cycles;
            }

            private Instruction(OpCode op, int val) {
                this.op = op;
                this.val = val;
            }

            private Instruction(OpCode op) {
                this.op = op;
                val = 0;
            }

            public String toString() {
                return this.op.toString() + " " + this.val;
            }
        }
    }

    private static class CRT {
        char[][] screen = new char[6][40];
        int pos = 0;

        public CRT() {

        }

        public void next(int spriteLoc) {
            int col = pos % 40;
            int row = pos / 40;

            if (col >= spriteLoc - 1 && col <= spriteLoc + 1) {
                screen[row][col] = '#';
            }
            pos++;
        }

        public String display() {
            String ret = "\n";

            for (char[] row : screen) {
                for (char c : row) {
                    if (c == 0) {
                        ret += " ";
                    }
                    else {
                        ret += c;
                    }
                }
                ret += "\n";
            }

            return ret;
        }
    }
}
