import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOIntegrationTest {

    @Test
    void shouldPerformStudentCrudOperations() throws SQLException {

        StudentDAO dao = new StudentDAO();

        int id = (int) (System.currentTimeMillis() % 1_000_000_000L) + 1;

        Student original = new Student(
                id, "Integration Test", 21, "Java"
        );

        try {
            // CREATE
            assertTrue(dao.addStudent(original),
                    "Student should be added");

            // READ
            Student found = dao.getStudentById(id);

            assertNotNull(found, "Student should be found");
            assertEquals("Integration Test", found.getName());
            assertEquals("Java", found.getCourse());

            // UPDATE
            Student updated = new Student(
                    id, "Updated Test", 22, "SQL"
            );

            assertTrue(dao.updateStudent(updated),
                    "Student should be updated");

            Student afterUpdate = dao.getStudentById(id);

            assertNotNull(afterUpdate);
            assertEquals("Updated Test", afterUpdate.getName());
            assertEquals(22, afterUpdate.getAge());
            assertEquals("SQL", afterUpdate.getCourse());

            // DELETE
            assertTrue(dao.deleteStudent(id),
                    "Student should be deleted");

            assertNull(dao.getStudentById(id),
                    "Deleted student should not exist");

        } finally {
            // Cleanup, even if an assertion fails
            Student remaining = dao.getStudentById(id);

            if (remaining != null) {
                dao.deleteStudent(id);
            }
        }
    }
}
