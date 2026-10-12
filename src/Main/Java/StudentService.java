import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private final StudentDAO dao;

    public StudentService() {
        this.dao = new StudentDAO();
    }

    public boolean addStudent(Student student) throws SQLException {
        validateStudent(student, true);
        return dao.addStudent(student);
    }

    public List<Student> getStudents(int page, int pageSize)
            throws SQLException {
        return dao.getStudents(page, pageSize);
    }

    public Student getStudentById(int id) throws SQLException {
        validateId(id);
        return dao.getStudentById(id);
    }

    public List<Student> searchStudentsByName(String name)
            throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Search name cannot be empty."
            );
        }

        return dao.searchStudentsByName(name.trim());
    }

    public boolean updateStudent(Student student) throws SQLException {
        validateStudent(student, false);
        return dao.updateStudent(student);
    }

    public boolean deleteStudent(int id) throws SQLException {
        validateId(id);
        return dao.deleteStudent(id);
    }

    private void validateStudent(Student student, boolean validateId)
            throws IllegalArgumentException {

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student cannot be null."
            );
        }

        if (validateId) {
            validateId(student.getId());
        } else {
            validateId(student.getId());
        }

        if (student.getName() == null
                || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Student name cannot be empty."
            );
        }

        if (student.getName().trim().length() > 50) {
            throw new IllegalArgumentException(
                    "Name cannot exceed 50 characters."
            );
        }

        if (student.getAge() < 1 || student.getAge() > 120) {
            throw new IllegalArgumentException(
                    "Age must be between 1 and 120."
            );
        }

        if (student.getCourse() == null
                || student.getCourse().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Course cannot be empty."
            );
        }

        if (student.getCourse().trim().length() > 50) {
            throw new IllegalArgumentException(
                    "Course cannot exceed 50 characters."
            );
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive."
            );
        }
    }
}