
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

class Patient {
    final String name;
    final int severity;
    final long arrival;

    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

public class Triage {

    private long counter = 0;

    // Lower severity number first; earlier arrival first
    private final PriorityQueue<Patient> queue =
        new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                      .thenComparingLong(p -> p.arrival)
        );

    void admit(String name, int severity) {
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException("Severity must be 1 to 5");
        }
        queue.add(new Patient(name, severity, counter++));
    }

    Patient next() {
        return queue.poll();
    }

    // Display patients in treatment order without removing them
    void displayWaitingList() {
        PriorityQueue<Patient> copy = new PriorityQueue<>(queue);

        System.out.println("Waiting list in treatment order:");
        while (!copy.isEmpty()) {
            System.out.print(copy.poll() + " ");
        }
        System.out.println();
    }

    // Measure adding and removing patients for different queue sizes
    static void growthTest() {
        int[] sizes = {1000, 5000, 10000};

        System.out.println("\nGROWTH EXPERIMENT");
        System.out.println("Patients\tAdd time (ms)\tRemove all (ms)");

        for (int size : sizes) {
            PriorityQueue<Patient> pq = new PriorityQueue<>();

            long startAdd = System.nanoTime();

            for (int i = 0; i < size; i++) {
                pq.add(new Patient("P" + i, (i % 5) + 1, i));
            }

            long endAdd = System.nanoTime();

            long startRemove = System.nanoTime();

            while (!pq.isEmpty()) {
                pq.poll();
            }

            long endRemove = System.nanoTime();

            double addTime = (endAdd - startAdd) / 1_000_000.0;
            double removeTime = (endRemove - startRemove) / 1_000_000.0;

            System.out.printf("%d\t\t%.3f\t\t%.3f%n",
                size, addTime, removeTime);
        }
    }

    public static void main(String[] args) {

        Triage er = new Triage();

        // Eight patients
        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);
        er.admit("Priya", 4);
        er.admit("Arun", 2);

        er.displayWaitingList();

        System.out.println("\nTREATMENT ORDER:");

        Patient p;
        while ((p = er.next()) != null) {
            System.out.print(p + " ");
        }

        System.out.println();

        growthTest();
    }
}
