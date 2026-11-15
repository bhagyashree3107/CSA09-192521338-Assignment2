
public class StringPerformance {

    static volatile int result;

    // Method 1: Using String
    static String buildUsingString(String[] fields) {
        String ticket = "";

        for (String field : fields) {
            ticket = ticket + field;
        }

        return ticket;
    }

    // Method 2: Using StringBuilder
    static String buildUsingStringBuilder(String[] fields) {
        StringBuilder ticket = new StringBuilder();

        for (String field : fields) {
            ticket.append(field);
        }

        return ticket.toString();
    }

    // Method 3: Using StringBuffer
    static String buildUsingStringBuffer(String[] fields) {
        StringBuffer ticket = new StringBuffer();

        for (String field : fields) {
            ticket.append(field);
        }

        return ticket.toString();
    }

    static String[] createFields(int count) {
        String[] fields = new String[count];

        for (int i = 0; i < count; i++) {
            fields[i] = "Field" + i + " ";
        }

        return fields;
    }

    static long testString(String[][] tickets) {
        long start = System.nanoTime();

        int total = 0;
        for (String[] fields : tickets) {
            total += buildUsingString(fields).length();
        }

        long end = System.nanoTime();
        result = total;

        return end - start;
    }

    static long testBuilder(String[][] tickets) {
        long start = System.nanoTime();

        int total = 0;
        for (String[] fields : tickets) {
            total += buildUsingStringBuilder(fields).length();
        }

        long end = System.nanoTime();
        result = total;

        return end - start;
    }

    static long testBuffer(String[][] tickets) {
        long start = System.nanoTime();

        int total = 0;
        for (String[] fields : tickets) {
            total += buildUsingStringBuffer(fields).length();
        }

        long end = System.nanoTime();
        result = total;

        return end - start;
    }

    static void runWorkload(String name, int ticketCount,
                            int fieldCount) {

        String[][] tickets = new String[ticketCount][];

        for (int i = 0; i < ticketCount; i++) {
            tickets[i] = createFields(fieldCount);
        }

        // Warm-up
        testString(tickets);
        testBuilder(tickets);
        testBuffer(tickets);

        int repetitions = 5;

        long stringTime = 0;
        long builderTime = 0;
        long bufferTime = 0;

        long memoryBefore = Runtime.getRuntime().totalMemory()
                          - Runtime.getRuntime().freeMemory();

        for (int i = 0; i < repetitions; i++) {
            stringTime += testString(tickets);
            builderTime += testBuilder(tickets);
            bufferTime += testBuffer(tickets);
        }

        long memoryAfter = Runtime.getRuntime().totalMemory()
                         - Runtime.getRuntime().freeMemory();

        System.out.println("\n" + name);
        System.out.println("--------------------------------");
        System.out.println("Tickets: " + ticketCount);
        System.out.println("Fields per ticket: " + fieldCount);

        System.out.printf("String average: %.3f ms%n",
                stringTime / repetitions / 1_000_000.0);

        System.out.printf("StringBuilder average: %.3f ms%n",
                builderTime / repetitions / 1_000_000.0);

        System.out.printf("StringBuffer average: %.3f ms%n",
                bufferTime / repetitions / 1_000_000.0);

        System.out.println("Approx. memory change: "
                + (memoryAfter - memoryBefore) + " bytes");

        System.out.println("Estimated intermediate concatenations "
                + "for String: "
                + (long) ticketCount * fieldCount);

        System.out.println("Estimated builder/buffer append operations: "
                + (long) ticketCount * fieldCount);
    }

    public static void main(String[] args) {

        System.out.println("RAILWAY E-TICKET PERFORMANCE TEST");
        System.out.println("=================================");

        runWorkload("WORKLOAD 1 - Small Ticket", 1, 6);

        runWorkload("WORKLOAD 2 - Medium Load", 1000, 50);

        runWorkload("WORKLOAD 3 - Heavy Load", 10000, 50);

        System.out.println("\nAll tests completed.");
    }
}
