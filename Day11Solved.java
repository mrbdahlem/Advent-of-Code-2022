import javax.naming.OperationNotSupportedException;
import java.util.*;
/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day11Solved extends DaySolution
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
    Monkey[] monkeys1; // used in part 1
    Monkey[] monkeys2; // used in part 2

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
        String[] monkeyDescription = input.split("\n\n");
        monkeys1 = new Monkey[monkeyDescription.length];
        monkeys2 = new Monkey[monkeyDescription.length];
        for (int i = 0; i < monkeyDescription.length; i++) {
            monkeys1[i] = new Monkey(monkeyDescription[i], true);
            monkeys2[i] = new Monkey(monkeyDescription[i], false);
        }
    }

    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        // Solve part 1 for this day here

        for (int round = 0; round < 20; round++) {
            for (Monkey monkey : monkeys1) {
                monkey.doInspection(monkeys1, 1);
            }

//            System.out.println("After round " + (round + 1));
//            for (int i = 0; i < monkeys1.length; i++) {
//                System.out.println("   Monkey " + i + " has " + monkeys1[i].items);
//                System.out.println("   Monkey " + i + " has inspected " + monkeys[i].numInspected() + " items");
//            }
        }

//        for (int i = 0; i < monkeys1.length; i++) {
//            System.out.println("   Monkey " + i + " has " + monkeys[i].items);
//            System.out.println("   Monkey " + i + " has inspected " + monkeys1[i].numInspected() + " items");
//        }

        return "" + findBusiestMonkeys(monkeys1);
    }
        
    /**
     * Solve part 2 of this day's problem.
     * @return the solution to part 2 of the problem
     */
    public String part2()
    {
        // Solve part 2 for this day here
        int modulo = 1;
        for (Monkey monkey : monkeys2) {
            modulo *= monkey.divisibilityTest;
        }

        for (int round = 0; round < 10000; round++) {
            for (Monkey monkey : monkeys2) {
                monkey.doInspection(monkeys2, modulo);
            }

//            if (round == 0 || round == 19 || ((round + 1) % 1000) == 0) {
//                System.out.println("After round " + (round + 1));
//                for (int i = 0; i < monkeys2.length; i++) {
////                    System.out.println("   Monkey " + i + " has " + monkeys2[i].items);
//                    System.out.println("   Monkey " + i + " has inspected " + monkeys2[i].numInspected() + " items");
//                }
//            }
        }

//        for (int i = 0; i < monkeys2.length; i++) {
//            System.out.println("   Monkey " + i + " has " + monkeys2[i].items);
//            System.out.println("   Monkey " + i + " has inspected " + monkeys2[i].numInspected() + " items");
//        }

        return "" + findBusiestMonkeys(monkeys2);
    }

    private long findBusiestMonkeys(Monkey[] monkeys) {
        long max1 = 0;
        long max2 = 0;

        for (Monkey monkey : monkeys) {
            long num = monkey.numInspected();
            if (num > max1) {
                max2 = max1;
                max1 = num;
            }
            else if (num > max2) {
                max2 = num;
            }
        }

        return (max1 * max2);
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

    static class Monkey {
        private final Deque<Long> items = new ArrayDeque<>();
        private final String[] equation;
        private final int divisibilityTest;
        private final int ifTrue;
        private final int ifFalse;

        private long hasInspected;

        private final boolean reduceWorry;


        /**
         * Parse a monkey description to determine how a monkey will handle items. Description formatted as:
         * Monkey 0:
         *   Starting items: 79, 98
         *   Operation: new = old * 19
         *   Test: divisible by 23
         *     If true: throw to monkey 2
         *     If false: throw to monkey 3
         *
         * @param description a string containing lines as shown above, separated by \n
         * @param reduceWorry if worry can be simply reduced by dividing by 3 for this part
         */
        public Monkey(String description, boolean reduceWorry) {
            this.reduceWorry = reduceWorry;
            String[] parts = description.split("\n");

            String[] itemsList = (parts[1].split(": "))[1].split(",");
            for (String item : itemsList) {
                items.add(Long.parseLong(item.trim()));
            }

            equation = (parts[2].split(" = "))[1].split(" ");

            String[] divisibility = (parts[3].split("divisible by "));
            divisibilityTest = Integer.parseInt(divisibility[1]);

            ifTrue = Integer.parseInt(parts[4].split("monkey ")[1]);
            ifFalse = Integer.parseInt(parts[5].split("monkey ")[1]);

            hasInspected = 0;
        }

        /**
         * @return the number of items this monkey has inspected
         */
        public long numInspected() {
            return hasInspected;
        }

        /**
         * Have this monkey conduct an inspection of the items it carries, in order.
         *
         * @param monkeys an array of monkeys this monkey can toss an item to
         * @param modulo a common divisor to use when reducing worry, 1 if none
         */
        public void doInspection(Monkey[] monkeys, int modulo) {
            int inspections = items.size();

            for (int i = 0; i < inspections; i++) {
                hasInspected++; // count the item as having been inspected

                if (items.isEmpty()) {
                    throw new RuntimeException("Monkey out of items");
                }
                long item = items.pollFirst();

                // Determine the new worry level based on this monkey's equation
                long op1 = operand(equation[0], item);
                long op2 = operand(equation[2], item);
                item = switch (equation[1]) {
                    case "+" -> op1 + op2;
                    case "*" -> op1 * op2;
                    default -> throw new RuntimeException(new OperationNotSupportedException(equation[1]));
                };

                // if worry can be simply reduced
                if (reduceWorry) {
                    item /= 3; // divide the worry level by 3
                }
                else {
                    item %= modulo; // otherwise reduce by common multiple so it can fit
                }

                // Determine which monkey to throw the item to
                int throwTo;
                if (item % divisibilityTest == 0) {
                    throwTo = ifTrue;
                }
                else {
                    throwTo = ifFalse;
                }

                // Toss the item to another monkey
                monkeys[throwTo].add(item);
            }
        }

        /**
         * an item has been tossed to this monkey
         * @param item the item the monkey has been tossed
         */
        public void add(long item) {
            items.addLast(item);
        }

        private long operand(String fromEquation, long oldVal) {
            if (fromEquation.equals("old")) {
                return oldVal;
            }
            return Integer.parseInt(fromEquation);
        }
    }
}
