import java.util.*;
/**
 * Solve one day of Advent of Code.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Day13Solved extends DaySolution
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
    String[] pairs;
    
    
    
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
        pairs = input.split("\n\n");
    }

    
    /**
     * Solve part 1 of this day's problem.
     * @return the solution to part 1 of the problem
     */
    public String part1()
    {
        String solution = "";
        // Solve part 1 for this day here
        int correct = 0;

        // Check each pair for the proper ordering
        for (int i = 0; i < pairs.length; i++) {
            String[] pair = pairs[i].split("\n");
            Packet firstPacket = new Packet(pair[0]);
//            System.out.println(firstPacket);

            Packet secondPacket = new Packet(pair[1]);
//            System.out.println(secondPacket);

            // if the first packet comes before the second, they are in the right order
            if (firstPacket.compareTo(secondPacket) <= 0) {
                correct += (i + 1);
            }
        }

        solution += correct;
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

        // This time, all packets go into a list, regardless of pairings
        List<Packet> packets = new ArrayList<>();
        for (String s : pairs) {
            String[] pair = s.split("\n");
            packets.add(new Packet(pair[0]));
            packets.add(new Packet(pair[1]));
        }

        // Add the two divider packets to the list, but keep a reference to find them back
        Packet divider2 = new Packet("[[2]]");
        Packet divider6 = new Packet("[[6]]");
        packets.add(divider2);
        packets.add(divider6);

        // Put all packets in the correct order
        packets.sort(Comparator.naturalOrder());

        // Find the locations of the 2 divider packets
        int pos2 = packets.indexOf(divider2) + 1;
        int pos6 = packets.indexOf(divider6) + 1;

        solution += (pos2 * pos6);
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


    private static class Packet implements Comparable<Packet>{
        Integer val;
        List<Packet> packetData;
        PacketType type;

        /**
         * Create a packet containing a single integer value
         * @param val the integer this packet will store
         */
        private Packet(Integer val) {
            this.val = val;
            this.type = PacketType.INTEGER;
        }

        /**
         * Create a packet from a String containing a comma separated list of packet contents, ie
         *       [[2],3,4,[5,[6,[],7]],8]
         * The packet data and any sub-packet lists of data must be surrounded by square brackets
         * @param data the data this packet will contain
         */
        public Packet(String data) {
            data = data.substring(1, data.length() - 1); // Strip off beginning/ending brackets

            packetData = new ArrayList<>();
            type = PacketType.LIST;

            int pos = 0;
            while (pos < data.length()) {
                char c = data.charAt(pos);

                String item = "";
                if (Character.isDigit(c)) {
                    while (pos < data.length() && Character.isDigit(data.charAt(pos))) {
                        item += data.charAt(pos);
                        pos++;
                    }
                    packetData.add(new Packet(Integer.parseInt(item)));
                    pos++;
                }
                else if (c == '[') {
                    int brackets = 1;
                    pos++;
                    item += c;
                    while (pos < data.length() && brackets > 0) {
                        c = data.charAt(pos);
                        if (c == '[') {
                            brackets++;
                        }
                        else if (c == ']') {
                            brackets--;
                        }
                        item += c;
                        pos++;
                    }
                    packetData.add(new Packet(item));
                    pos++;
                }
                else {
                    System.out.println("Unexpected character in packet data: " +
                            data.substring(0, pos) + ">>>" + c + "<<<" + data.substring(pos + 1));
                    pos++;
                }
            }

        }

        /**
         * Compares this packet with the specified packet for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         */
        @Override
        public int compareTo(Packet other) {
            if (this.type == PacketType.INTEGER && other.type == PacketType.INTEGER) {
                return this.val - other.val;
            }
            if (this.type == PacketType.LIST && other.type == PacketType.LIST) {
                for (int i = 0; i < this.packetData.size() && i < other.packetData.size(); i++) {
                    int comparison = packetData.get(i).compareTo(other.packetData.get(i));

                    if (comparison != 0) {
                        return comparison;
                    }
                }
                return this.packetData.size() - other.packetData.size();
            }
            if (this.type == PacketType.INTEGER && other.type == PacketType.LIST) {
                Packet thisAsList = Packet.asList(this.val);
                return thisAsList.compareTo(other);
            }

            // if (this.type == PacketType.LIST && other.type == PacketType.INTEGER) {
            Packet otherAsList = Packet.asList(other.val);
            return this.compareTo(otherAsList);

        }

        /**
         * Create a list packet from an integer value
         * @param val the value to store in the list
         * @return the new packet containing the integer as a list
         */
        private static Packet asList(Integer val) {
            Packet p = new Packet("[" + val + "]");
//            System.out.println(p);
            return p;
        }

        public String toString() {
            String s = type.name() + ": ";
            if (type == PacketType.INTEGER) {
                s += val;
            }
            else {
                s += packetData.toString();
            }

            return s;
        }

        private enum PacketType {
            INTEGER, LIST
        }
    }
}
