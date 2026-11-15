
import java.util.*;

public class ListComparison {

    static volatile long result;

    static ArrayList<Integer> makeArrayList(int n) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(i);
        return list;
    }

    static LinkedList<Integer> makeLinkedList(int n) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) list.add(i);
        return list;
    }

    static ArrayDeque<Integer> makeArrayDeque(int n) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) deque.addLast(i);
        return deque;
    }

    static long timeAddEnd(int type, int n) {
        long start = System.nanoTime();

        if (type == 1) {
            ArrayList<Integer> list = makeArrayList(n);
            list.add(n);
            result = list.size();
        } else if (type == 2) {
            LinkedList<Integer> list = makeLinkedList(n);
            list.add(n);
            result = list.size();
        } else {
            ArrayDeque<Integer> deque = makeArrayDeque(n);
            deque.addLast(n);
            result = deque.size();
        }

        return System.nanoTime() - start;
    }

    static long timeFrontOperations(int type, int n) {
        long start = System.nanoTime();

        if (type == 1) {
            ArrayList<Integer> list = makeArrayList(n);
            list.add(0, -1);
            list.remove(0);
            result = list.size();
        } else if (type == 2) {
            LinkedList<Integer> list = makeLinkedList(n);
            list.addFirst(-1);
            list.removeFirst();
            result = list.size();
        } else {
            ArrayDeque<Integer> deque = makeArrayDeque(n);
            deque.addFirst(-1);
            deque.removeFirst();
            result = deque.size();
        }

        return System.nanoTime() - start;
    }

    static long timeMiddleOperations(int type, int n) {
        long start = System.nanoTime();

        if (type == 1) {
            ArrayList<Integer> list = makeArrayList(n);
            int mid = list.size() / 2;
            list.add(mid, -1);
            list.remove(mid);
            result = list.size();
        } else if (type == 2) {
            LinkedList<Integer> list = makeLinkedList(n);
            int mid = list.size() / 2;
            list.add(mid, -1);
            list.remove(mid);
            result = list.size();
        }

        return System.nanoTime() - start;
    }

    static long timeGetMiddle(int type, int n) {
        long start = System.nanoTime();

        if (type == 1) {
            ArrayList<Integer> list = makeArrayList(n);
            result = list.get(n / 2);
        } else if (type == 2) {
            LinkedList<Integer> list = makeLinkedList(n);
            result = list.get(n / 2);
        } else {
            ArrayDeque<Integer> deque = makeArrayDeque(n);
            Iterator<Integer> it = deque.iterator();
            int value = 0;
            for (int i = 0; i <= n / 2; i++) {
                value = it.next();
            }
            result = value;
        }

        return System.nanoTime() - start;
    }

    static long timeIteration(int type, int n) {
        long start = System.nanoTime();
        long sum = 0;

        if (type == 1) {
            for (int value : makeArrayList(n)) sum += value;
        } else if (type == 2) {
            for (int value : makeLinkedList(n)) sum += value;
        } else {
            for (int value : makeArrayDeque(n)) sum += value;
        }

        result = sum;
        return System.nanoTime() - start;
    }

    static void printTime(String operation, int type, int n) {
        long total = 0;
        int repetitions = 5;

        for (int i = 0; i < repetitions; i++) {
            if (operation.equals("End add"))
                total += timeAddEnd(type, n);
            else if (operation.equals("Front add/remove"))
                total += timeFrontOperations(type, n);
            else if (operation.equals("Middle insert/remove"))
                total += timeMiddleOperations(type, n);
            else if (operation.equals("Get middle"))
                total += timeGetMiddle(type, n);
            else if (operation.equals("Full iteration"))
                total += timeIteration(type, n);
        }

        System.out.printf("%-23s %12.3f ms%n",
                operation, total / repetitions / 1_000_000.0);
    }

    static void runTests(int n) {
        System.out.println("\n======================================");
        System.out.println("PLAYLIST SIZE: " + n);
        System.out.println("======================================");

        String[] names = {"ArrayList", "LinkedList", "ArrayDeque"};

        for (int type = 1; type <= 3; type++) {
            System.out.println("\n" + names[type - 1]);
            printTime("End add", type, n);
            printTime("Front add/remove", type, n);

            if (type != 3)
                printTime("Middle insert/remove", type, n);
            else
                System.out.println("Middle insert/remove: Not directly supported");

            printTime("Get middle", type, n);
            printTime("Full iteration", type, n);
        }
    }

    public static void main(String[] args) {
        System.out.println("ARRAYLIST vs LINKEDLIST vs ARRAYDEQUE");

        runTests(1000);
        runTests(5000);
        runTests(10000);

        System.out.println("\nAll experiments completed.");
    }
}
