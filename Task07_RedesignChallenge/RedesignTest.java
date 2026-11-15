
public class RedesignTest {

    // Original approach: repeated String concatenation
    static String original(int tickets, int fields) {
        String result = "";

        for (int i = 0; i < tickets; i++) {
            for (int j = 0; j < fields; j++) {
                result = result + "FIELD" + j + " ";
            }
        }

        return result;
    }

    // Redesigned approach: pre-sized StringBuilder
    static String redesigned(int tickets, int fields) {
        int capacity = tickets * fields * 10;
        StringBuilder result = new StringBuilder(capacity);

        for (int i = 0; i < tickets; i++) {
            for (int j = 0; j < fields; j++) {
                result.append("FIELD").append(j).append(" ");
            }
        }

        return result.toString();
    }

    static double measureOriginal(int tickets, int fields) {
        long start = System.nanoTime();
        String result = original(tickets, fields);
        long end = System.nanoTime();

        return (end - start) / 1_000_000.0;
    }

    static double measureRedesigned(int tickets, int fields) {
        long start = System.nanoTime();
        String result = redesigned(tickets, fields);
        long end = System.nanoTime();

        return (end - start) / 1_000_000.0;
    }

    public static void main(String[] args) {

        int[][] workloads = {
            {1, 6},
            {1000, 50},
            {10000, 50}
        };

        System.out.println("ORIGINAL vs REDESIGNED");
        System.out.println("Tickets\tFields\tOriginal(ms)\tRedesigned(ms)");

        for (int[] w : workloads) {
            int tickets = w[0];
            int fields = w[1];

            // Warm-up
            measureOriginal(1, 6);
            measureRedesigned(1, 6);

            double oldTotal = 0;
            double newTotal = 0;

            for (int run = 0; run < 5; run++) {
                oldTotal += measureOriginal(tickets, fields);
                newTotal += measureRedesigned(tickets, fields);
            }

            System.out.printf("%d\t%d\t%.3f\t\t%.3f%n",
                tickets, fields, oldTotal / 5, newTotal / 5);

            boolean same = original(tickets, fields)
                    .equals(redesigned(tickets, fields));

            System.out.println("Output identical: " + same);
        }

        System.out.println("All tests completed.");
    }
}
