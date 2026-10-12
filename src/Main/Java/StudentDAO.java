import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDAO {

    private static final Logger LOGGER =
            Logger.getLogger(StudentDAO.class.getName());

    // CREATE: Add a new student
    public boolean addStudent(Student student) throws SQLException {

        validateStudent(student);

        String sql = "INSERT INTO students (id, name, age, course) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, student.getId());
            ps.setString(2, student.getName().trim());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getCourse().trim());

            boolean success = ps.executeUpdate() == 1;

            if (success) {
                LOGGER.info("Student added successfully. ID: "
                        + student.getId());
            } else {
                LOGGER.warning("Student was not added.");
            }

            return success;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add student.", e);
            throw e;
        }
    }

    // READ ALL: Retrieve students with pagination
    public List<Student> getStudents(int page, int pageSize)
            throws SQLException {

        if (page < 1 || pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException(
                    "Page must be positive and pageSize must be 1-100."
            );
        }

        String sql = "SELECT id, name, age, course FROM students "
                + "ORDER BY id ASC LIMIT ? OFFSET ?";

        List<Student> students = new ArrayList<>();
        long offset = ((long) page - 1) * pageSize;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setLong(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }

            LOGGER.info("Retrieved " + students.size()
                    + " students. Page: " + page);

            return students;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve students.", e);
            throw e;
        }
    }

    // READ ONE: Find student by ID
    public Student getStudentById(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive."
            );
        }

        String sql = "SELECT id, name, age, course "
                + "FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.info("Student found. ID: " + id);
                    return mapStudent(rs);
                }

                LOGGER.info("Student not found. ID: " + id);
                return null;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to find student.", e);
            throw e;
        }
    }

    // SEARCH: Find students by name
    public List<Student> searchStudentsByName(String name)
            throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Search name cannot be empty."
            );
        }

        String sql = "SELECT id, name, age, course FROM students "
                + "WHERE name LIKE ? ORDER BY name ASC, id ASC";

        List<Student> students = new ArrayList<>();
        String searchName = name.trim();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + searchName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }

            LOGGER.info("Student search completed. Results: "
                    + students.size());

            return students;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to search students.", e);
            throw e;
        }
    }

    // UPDATE: Modify an existing student
    public boolean updateStudent(Student student) throws SQLException {

        validateStudent(student);

        String sql = "UPDATE students "
                + "SET name = ?, age = ?, course = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getName().trim());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getCourse().trim());
            ps.setInt(4, student.getId());

            boolean success = ps.executeUpdate() == 1;

            if (success) {
                LOGGER.info("Student updated successfully. ID: "
                        + student.getId());
            } else {
                LOGGER.warning("No student updated. ID: "
                        + student.getId());
            }

            return success;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update student.", e);
            throw e;
        }
    }

    // DELETE: Remove student by ID
    public boolean deleteStudent(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive."
            );
        }

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            boolean success = ps.executeUpdate() == 1;

            if (success) {
                LOGGER.info("Student deleted successfully. ID: " + id);
            } else {
                LOGGER.warning("No student deleted. ID: " + id);
            }

            return success;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete student.", e);
            throw e;
        }
    }

    // VALIDATION: Shared validation for add and update
    private void validateStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student cannot be null."
            );
        }

        if (student.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive."
            );
        }

        if (student.getName() == null
                || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Student name cannot be empty."
            );
        }

        if (student.getName().trim().length() > 50) {
            throw new IllegalArgumentException(
                    "Student name cannot exceed 50 characters."
            );
        }

        if (student.getAge() < 1 || student.getAge() > 120) {
            throw new IllegalArgumentException(
                    "Student age must be between 1 and 120."
            );
        }

        if (student.getCourse() == null
                || student.getCourse().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Student course cannot be empty."
            );
        }

        if (student.getCourse().trim().length() > 50) {
            throw new IllegalArgumentException(
                    "Student course cannot exceed 50 characters."
            );
        }
    }

    // MAP: Convert database row to Student object
    private Student mapStudent(ResultSet rs) throws SQLException {

        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("course")
        );
    }
}