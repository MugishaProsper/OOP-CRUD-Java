import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:students.db";

    // Establish connection to SQLite database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Create students table if it doesn't exist
    public static void createStudentsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students (\n"
                + " id integer PRIMARY KEY,\n"
                + " firstName text NOT NULL,\n"
                + " lastName text NOT NULL,\n"
                + " age integer,\n"
                + " gender text,\n"
                + " combination text\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Create (Insert) a new student into the database
    public static void insertStudent(Student student) {
        String sql = "INSERT INTO students(id, firstName, lastName, age, gender, combination) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, student.getId());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setInt(4, student.getAge());
            pstmt.setString(5, student.getGender());
            pstmt.setString(6, student.getCombination());

            pstmt.executeUpdate();
            System.out.println("Student inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Read (Retrieve) a student by ID
    public static Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        Student student = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                student = new Student(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("combination")
                );
                System.out.println("Student found: " + student.getFirstName() + " " + student.getLastName());
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return student;
    }

    // Retrieve all students
    public static List<Student> getAllStudents() {
        String sql = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("combination")
                );
                students.add(student);
            }

            if (students.isEmpty()) {
                System.out.println("No students found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    // Update a student's information
    public static void updateStudent(Student student) {
        String sql = "UPDATE students SET firstName = ?, lastName = ?, age = ?, gender = ?, combination = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getCombination());
            pstmt.setInt(6, student.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student with ID " + student.getId() + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete a student by ID
    public static void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Student with ID " + id + " deleted.");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create students table
        createStudentsTable();

        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("\nCRUD Operations Menu:");
            System.out.println("1. Insert Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Specific Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("q. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.next();

            switch (choice) {
                case "1":
                    // Insert a student
                    System.out.print("Enter student ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter gender: ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter combination: ");
                    String combination = scanner.nextLine();

                    Student student = new Student(id, firstName, lastName, age, gender, combination);
                    insertStudent(student);
                    break;

                case "2":
                    // View all students
                    List<Student> allStudents = getAllStudents();
                    System.out.println("=== Students in database ===\n");
                    for (Student s : allStudents) {
                        System.out.println("ID: " + s.getId() +
                                ", Name: " + s.getFirstName() + " " + s.getLastName() +
                                ", Age: " + s.getAge() +
                                ", Gender: " + s.getGender() +
                                ", Combination: " + s.getCombination());
                    }
                    break;

                case "3":
                    // View a specific student
                    System.out.print("Enter student ID to view: ");
                    int viewId = scanner.nextInt();
                    Student studentWithThisId = getStudentById(viewId);
                    if (studentWithThisId != null) {
                        System.out.println("Student Details: ");
                        System.out.println("ID: " + studentWithThisId.getId());
                        System.out.println("Name: " + studentWithThisId.getFirstName() + " " + studentWithThisId.getLastName());
                        System.out.println("Age: " + studentWithThisId.getAge());
                        System.out.println("Gender: " + studentWithThisId.getGender());
                        System.out.println("Combination: " + studentWithThisId.getCombination());
                    }
                    break;

                case "4":
                    // Update a student
                    System.out.print("Enter student ID to update: ");
                    int updateId = scanner.nextInt();
                    Student studentToUpdate = getStudentById(updateId);
                    if (studentToUpdate != null) {
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new first name: ");
                        studentToUpdate.setFirstName(scanner.nextLine());
                        System.out.print("Enter new last name: ");
                        studentToUpdate.setLastName(scanner.nextLine());
                        System.out.print("Enter new age: ");
                        studentToUpdate.setAge(scanner.nextInt());
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new gender: ");
                        studentToUpdate.setGender(scanner.nextLine());
                        System.out.print("Enter new combination: ");
                        studentToUpdate.setCombination(scanner.nextLine());

                        updateStudent(studentToUpdate);
                    }
                    break;

                case "5":
                    // Delete a student
                    System.out.print("Enter student ID to delete: ");
                    int deleteId = scanner.nextInt();
                    deleteStudent(deleteId);
                    break;

                case "q":
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("q"));

        scanner.close();
    }
}
