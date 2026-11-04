import java.sql.*;
import java.util.*;

public class StudentDatabaseApp {
    private static final String DB_URL = "jdbc:h2:./studentdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            createTableIfNotExists(conn);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nStudent Database CLI");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addStudent(conn, scanner);
                    case 2 -> viewStudents(conn);
                    case 3 -> updateStudent(conn, scanner);
                    case 4 -> deleteStudent(conn, scanner);
                    case 5 -> { System.out.println("Exiting."); return; }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNotExists(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Student (" +
                "student_id INT AUTO_INCREMENT PRIMARY KEY," +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "dob DATE," +
                "department VARCHAR(50)," +
                "phone_number VARCHAR(20)," +
                "student_accadamic_start_year INT," +
                "student_accadamic_end_year INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("DOB (yyyy-mm-dd): ");
        String dob = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Academic Start Year: ");
        int startYear = Integer.parseInt(scanner.nextLine());
        System.out.print("Academic End Year: ");
        int endYear = Integer.parseInt(scanner.nextLine());
        String sql = "INSERT INTO Student (first_name, last_name, dob, department, phone_number, student_accadamic_start_year, student_accadamic_end_year) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setDate(3, java.sql.Date.valueOf(dob));
            ps.setString(4, department);
            ps.setString(5, phoneNumber);
            ps.setInt(6, startYear);
            ps.setInt(7, endYear);
            ps.executeUpdate();
            System.out.println("Student added.");
        }
    }

    private static void viewStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Student";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-5s %-12s %-12s %-12s %-15s %-15s %-8s %-8s\n", "ID", "First Name", "Last Name", "DOB", "Department", "Phone", "Start", "End");
            while (rs.next()) {
                System.out.printf("%-5d %-12s %-12s %-12s %-15s %-15s %-8d %-8d\n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("dob"),
                        rs.getString("department"),
                        rs.getString("phone_number"),
                        rs.getInt("student_accadamic_start_year"),
                        rs.getInt("student_accadamic_end_year"));
            }
        }
    }

    private static void updateStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("New First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("New Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("New DOB (yyyy-mm-dd): ");
        String dob = scanner.nextLine();
        System.out.print("New Department: ");
        String department = scanner.nextLine();
        System.out.print("New Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("New Academic Start Year: ");
        int startYear = Integer.parseInt(scanner.nextLine());
        System.out.print("New Academic End Year: ");
        int endYear = Integer.parseInt(scanner.nextLine());
        String sql = "UPDATE Student SET first_name=?, last_name=?, dob=?, department=?, phone_number=?, student_accadamic_start_year=?, student_accadamic_end_year=? WHERE student_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setDate(3, java.sql.Date.valueOf(dob));
            ps.setString(4, department);
            ps.setString(5, phoneNumber);
            ps.setInt(6, startYear);
            ps.setInt(7, endYear);
            ps.setInt(8, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Student updated.");
            else System.out.println("Student not found.");
        }
    }

    private static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        String sql = "DELETE FROM Student WHERE student_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Student deleted.");
            else System.out.println("Student not found.");
        }
    }
}
