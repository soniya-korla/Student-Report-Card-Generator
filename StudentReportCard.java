import java.io.*;
import java.util.*;

public class StudentReportCard {

    static class Student {
        String name;
        int rollNo;
        int[] marks = new int[3]; // marks for 3 subjects
        double average;
        String grade;

        Student(String name, int rollNo, int[] marks) {
            this.name = name;
            this.rollNo = rollNo;
            this.marks = marks;
            calculateAverageAndGrade();
        }

        void calculateAverageAndGrade() {
            int total = 0;
            for (int mark : marks) total += mark;
            average = total / 3.0;
            if (average >= 90) grade = "A";
            else if (average >= 75) grade = "B";
            else if (average >= 60) grade = "C";
            else grade = "D";
        }

        @Override
        public String toString() {
            return rollNo + "," + name + "," + marks[0] + "," + marks[1] + "," + marks[2] + "," + average + "," + grade;
        }
    }

    static final String FILE_NAME = "students_report.csv";
    static List<Student> students = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        loadFromFile();

        while (true) {
            System.out.println("\n--- Student Report Card Generator ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Reports");
            System.out.println("3. View Topper");
            System.out.println("4. Save & Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewAll();
                case 3 -> viewTopper();
                case 4 -> {
                    saveToFile();
                    System.out.println("Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void addStudent(Scanner sc) {
        System.out.print("Enter name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter roll number: ");
        int roll = sc.nextInt();
        int[] marks = new int[3];
        for (int i = 0; i < 3; i++) {
            System.out.print("Enter marks for subject " + (i + 1) + ": ");
            marks[i] = sc.nextInt();
        }
        students.add(new Student(name, roll, marks));
        System.out.println("Student added successfully.");
    }

    static void viewAll() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println("\nRollNo,Name,Sub1,Sub2,Sub3,Average,Grade");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    static void viewTopper() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        Student top = Collections.max(students, Comparator.comparingDouble(s -> s.average));
        System.out.println("\nTopper: " + top.name + " (Roll No: " + top.rollNo + ", Avg: " + top.average + ", Grade: " + top.grade + ")");
    }

    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                pw.println(s);
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[1];
                int roll = Integer.parseInt(data[0]);
                int[] marks = {Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4])};
                students.add(new Student(name, roll, marks));
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}
