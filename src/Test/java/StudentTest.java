
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void studentGettersShouldReturnCorrectValues() {
        Student student = new Student(
                1, "Rahul", 21, "Java"
        );

        assertEquals(1, student.getId());
        assertEquals("Rahul", student.getName());
        assertEquals(21, student.getAge());
        assertEquals("Java", student.getCourse());
    }

    @Test
    void studentSettersShouldUpdateValues() {
        Student student = new Student(
                1, "Rahul", 21, "Java"
        );

        student.setName("Amit");
        student.setAge(22);
        student.setCourse("Python");

        assertEquals("Amit", student.getName());
        assertEquals(22, student.getAge());
        assertEquals("Python", student.getCourse());
    }
}