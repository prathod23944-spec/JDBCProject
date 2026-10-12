import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentManagementSystem {

    private static final Logger LOGGER =
            Logger.getLogger(StudentManagementSystem.class.getName());

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService service = new StudentService();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showMenu();

            try {
                int choice = readInt("Enter choice: ");

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        viewStudents();
                        break;
                    case 3:
                        searchStudent();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        deleteStudent();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Goodbye, bro!");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Validation error: " + e.getMessage());

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database operation failed", e);
                System.out.println(
                        "Database operation failed. Please check your input "
                                + "or database connection."
                );

                if (e.getErrorCode() == 1062) {
                    System.out.println(
                            "This Student ID already exists."
                    );
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected application error", e);
                System.out.println("Unexpected error occurred.");
            }
        }

        try {
            DBConnection.closePool();
        } finally {
            scanner.close();
        }
    }

    private static void showMenu() {
        System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Student");
        System.out.println("2. View Students (Pagination)");
        System.out.println("3. Search Student by Name");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Exit");
    }

    private static void addStudent() throws SQLException {
        int id = readInt("Enter ID: ");
        String name = readText("Enter name: ");
        int age = readInt("Enter age: ");
        String course = readText("Enter course: ");

        Student student = new Student(id, name, age, course);

        if (service.addStudent(student)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Student was not added.");
        }
    }

    private static void viewStudents() throws SQLException {
        int page = readInt("Enter page number (1, 2, 3...): ");
        int pageSize = readInt("Students per page (1-100): ");

        List<Student> students = service.getStudents(page, pageSize);

        if (students.isEmpty()) {
            System.out.println("No students found on this page.");
            return;
        }

        System.out.println("\n--- Students ---");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    private static void searchStudent() throws SQLException {
        String name = readText("Enter name to search: ");

        List<Student> students =
                service.searchStudentsByName(name);

        if (students.isEmpty()) {
            System.out.println("No matching students found.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }

    private static void updateStudent() throws SQLException {
        int id = readInt("Enter student ID to update: ");

        Student existing = service.getStudentById(id);

        if (existing == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("Current details: " + existing);

        String name = readText("Enter new name: ");
        int age = readInt("Enter new age: ");
        String course = readText("Enter new course: ");

        Student updated = new Student(id, name, age, course);

        if (service.updateStudent(updated)) {
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student was not updated.");
        }
    }

    private static void deleteStudent() throws SQLException {
        int id = readInt("Enter student ID to delete: ");

        Student existing = service.getStudentById(id);

        if (existing == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println("Student: " + existing);

        String confirm = readText("Delete this student? (yes/no): ");

        if (!"yes".equalsIgnoreCase(confirm.trim())) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (service.deleteStudent(id)) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student was not deleted.");
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid whole number."
            );
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}