
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Student {
    int id;
    String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Student))
            return false;

        Student s = (Student) obj;
        return this.id == s.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student ID: " + id + ", Name: " + name;
    }
}

public class HashSetContract {
    public static void main(String[] args) {

        Set<Student> students = new HashSet<>();

        Student s1 = new Student(101, "Arun");
        Student s2 = new Student(102, "Bala");
        Student s3 = new Student(101, "Arun Duplicate");
        Student s4 = new Student(103, "Charan");

        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);

        System.out.println("HashSet Contents:");

        for (Student s : students) {
            System.out.println(s);
        }

        System.out.println("\nTotal students: " + students.size());

        System.out.println("\nChecking duplicate:");
        System.out.println("s1 equals s3: " + s1.equals(s3));

        System.out.println("\nHash codes:");
        System.out.println("s1: " + s1.hashCode());
        System.out.println("s3: " + s3.hashCode());
    }
}
