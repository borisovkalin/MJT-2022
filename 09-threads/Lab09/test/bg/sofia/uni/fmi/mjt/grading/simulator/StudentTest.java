package bg.sofia.uni.fmi.mjt.grading.simulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StudentTest {
    @Test
    void testStudentConstructor() {
        Student student = new Student(1,"k",null);
        assertEquals(1,student.getFn(),"Expected correct fn");
        assertEquals("k",student.getName(), "Expected correct name");
        assertNull(student.getGrader(), "Expected null grader");
    }
}
