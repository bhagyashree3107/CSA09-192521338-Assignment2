
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Team {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {
    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) {
        items.add(item);
    }

    // Accepts a list of T or any subtype of T
    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }

    List<T> ranking() {
        List<T> result = new ArrayList<>(items);
        result.sort(order);
        return result;
    }
}

public class LeaderboardDemo {

    public static void main(String[] args) {

        // Higher points, higher goal difference, alphabetical name
        Comparator<Team> byRank =
            Comparator.comparingInt((Team t) -> t.points).reversed()
            .thenComparing(
                Comparator.comparingInt((Team t) -> t.goalDiff).reversed()
            )
            .thenComparing(t -> t.name);

        Leaderboard<Team> board = new Leaderboard<>(byRank);

        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));

        System.out.println("MAIN LEADERBOARD:");
        System.out.println(board.ranking());

        // Second test: ties at points and goal difference
        List<Team> testTeams = new ArrayList<>();

        testTeams.add(new Team("Zebras", 12, 5));
        testTeams.add(new Team("Lions", 12, 8));
        testTeams.add(new Team("Bears", 12, 8));
        testTeams.add(new Team("Tigers", 12, 5));
        testTeams.add(new Team("Wolves", 8, 3));

        Leaderboard<Team> testBoard = new Leaderboard<>(byRank);
        testBoard.addAll(testTeams);

        System.out.println("\nSECOND TEST - MULTIPLE TIES:");
        System.out.println(testBoard.ranking());

        // Deliberately faulty comparator: ignores goal difference
        Comparator<Team> faultyOrder =
            Comparator.comparingInt((Team t) -> t.points).reversed();

        Leaderboard<Team> faultyBoard =
            new Leaderboard<>(faultyOrder);

        faultyBoard.addAll(testTeams);

        System.out.println("\nFAULTY COMPARATOR RESULT:");
        System.out.println(faultyBoard.ranking());

        System.out.println("\nOriginal insertion order:");
        System.out.println(testTeams);
    }
}
