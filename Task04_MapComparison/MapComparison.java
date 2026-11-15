
import java.util.*;

public class MapComparison {

    static final int LOOKUPS = 100000;

    static String medicineCode(int i) {
        return String.format("MED%05d", i);
    }

    static Map<String, Integer> createHashMap(int n) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = n - 1; i >= 0; i--) {
            map.put(medicineCode(i), 100 + i);
        }
        return map;
    }

    static Map<String, Integer> createTreeMap(int n) {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = n - 1; i >= 0; i--) {
            map.put(medicineCode(i), 100 + i);
        }
        return map;
    }

    static Map<String, Integer> createLinkedHashMap(int n) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int i = n - 1; i >= 0; i--) {
            map.put(medicineCode(i), 100 + i);
        }
        return map;
    }

    static long lookupTime(Map<String, Integer> map, int n) {
        long sum = 0;
        long start = System.nanoTime();

        for (int i = 0; i < LOOKUPS; i++) {
            String code = medicineCode(i % n);
            sum += map.get(code);
        }

        long end = System.nanoTime();

        // Prevent the lookup results from being unused
        if (sum == -1) System.out.println(sum);

        return end - start;
    }

    static long codeReportTime(Map<String, Integer> map) {
        long start = System.nanoTime();

        List<String> codes = new ArrayList<>(map.keySet());
        Collections.sort(codes);

        long end = System.nanoTime();

        return end - start;
    }

    static long insertionReportTime(Map<String, Integer> map,
                                    List<String> insertionOrder) {
        long start = System.nanoTime();

        List<String> report = new ArrayList<>();

        if (map instanceof LinkedHashMap) {
            report.addAll(map.keySet());
        } else {
            report.addAll(insertionOrder);
        }

        long end = System.nanoTime();

        return end - start;
    }

    static void test(int n) {

        Map<String, Integer> hashMap = createHashMap(n);
        Map<String, Integer> treeMap = createTreeMap(n);
        Map<String, Integer> linkedMap = createLinkedHashMap(n);

        List<String> insertionOrder = new ArrayList<>();

        for (int i = n - 1; i >= 0; i--) {
            insertionOrder.add(medicineCode(i));
        }

        // Warm-up lookups
        lookupTime(hashMap, n);
        lookupTime(treeMap, n);
        lookupTime(linkedMap, n);

        System.out.println("\n==================================");
        System.out.println("INVENTORY SIZE: " + n);
        System.out.println("==================================");

        System.out.println("\n100,000 LOOKUP TIMES");
        System.out.printf("HashMap: %.3f ms%n",
                lookupTime(hashMap, n) / 1_000_000.0);
        System.out.printf("TreeMap: %.3f ms%n",
                lookupTime(treeMap, n) / 1_000_000.0);
        System.out.printf("LinkedHashMap: %.3f ms%n",
                lookupTime(linkedMap, n) / 1_000_000.0);

        System.out.println("\nCODE-ORDER REPORT TIMES");
        System.out.printf("HashMap (sort keys): %.3f ms%n",
                codeReportTime(hashMap) / 1_000_000.0);
        System.out.printf("TreeMap (natural order): %.3f ms%n",
                codeReportTime(treeMap) / 1_000_000.0);
        System.out.printf("LinkedHashMap (sort keys): %.3f ms%n",
                codeReportTime(linkedMap) / 1_000_000.0);

        System.out.println("\nINSERTION-ORDER REPORT TIMES");
        System.out.printf("HashMap (external order list): %.3f ms%n",
                insertionReportTime(hashMap, insertionOrder) / 1_000_000.0);
        System.out.printf("TreeMap (external order list): %.3f ms%n",
                insertionReportTime(treeMap, insertionOrder) / 1_000_000.0);
        System.out.printf("LinkedHashMap (natural order): %.3f ms%n",
                insertionReportTime(linkedMap, insertionOrder) / 1_000_000.0);

        System.out.println("\nORDER VERIFICATION");
        System.out.println("HashMap code order: "
                + isSorted(new ArrayList<>(hashMap.keySet())));
        System.out.println("TreeMap code order: "
                + isSorted(new ArrayList<>(treeMap.keySet())));
        System.out.println("LinkedHashMap insertion order: "
                + new ArrayList<>(linkedMap.keySet()).equals(insertionOrder));
    }

    static boolean isSorted(List<String> list) {
        List<String> copy = new ArrayList<>(list);
        Collections.sort(copy);
        return list.equals(copy);
    }

    public static void main(String[] args) {

        System.out.println("PHARMACY INVENTORY MAP COMPARISON");

        int[] sizes = {1000, 5000, 10000};

        for (int n : sizes) {
            test(n);
        }

        System.out.println("\nAll experiments completed.");
    }
}
