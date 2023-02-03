package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNull;

public class CodePostGraderTest {
    @Test
    void testGetAssignmentWithNoAssignments() {
        CodePostGrader grader = new CodePostGrader(5);
        grader.finalizeGrading();
        assertNull(grader.getAssignment(),"Expected method to return null");
    }

   @Test
    void testGetAssignmentWithOneAssignments() throws InterruptedException {
        CodePostGrader grader = new CodePostGrader(5);
        Thread student = new Thread(new Student(1,"k", grader));
        student.start();
        student.join();
        grader.finalizeGrading();
        int result = 0;
        for (var assist :grader.getAssistants()) {
            assist.join();
            System.out.println(assist.getAssistantName() + " :" + assist.getNumberOfGradedAssignments());
            result += assist.getNumberOfGradedAssignments();
        }
        assertEquals(1,result, "Expected 1 graded assignment");
        assertNull(grader.getAssignment(),"Expected method to return null");
    }

    @Test
    void testGetSubmittedAssignmentsCount() throws InterruptedException {
        CodePostGrader grader = new CodePostGrader(5);
        Thread student = new Thread(new Student(1,"k", grader));
        student.start();
        student.join();
        grader.finalizeGrading();
        for (var assist :grader.getAssistants()) {
            assist.join();
        }
        assertEquals(1,grader.getSubmittedAssignmentsCount(), "Expected 1 graded assignment");
        assertNull(grader.getAssignment(),"Expected method to return null");
    }

}
