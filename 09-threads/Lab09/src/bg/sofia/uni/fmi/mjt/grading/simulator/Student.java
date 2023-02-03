package bg.sofia.uni.fmi.mjt.grading.simulator;


import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Student implements Runnable {
    private static final Random  RANDOM = new Random();
    private static final int MAX_ASSIGNMENT_TIME = 1001;
    private static final int TYPESET = AssignmentType.values().length;
    private final int fn;
    private final String name;
    private final StudentGradingAPI studentGradingAPI;

    public Student(int fn, String name, StudentGradingAPI studentGradingAPI) {
        this.fn = fn;
        this.name = name;
        this.studentGradingAPI = studentGradingAPI;
    }

    @Override
    public void run() {
        AssignmentType type = AssignmentType.values()[RANDOM.nextInt(TYPESET)];
        Assignment assignment = new Assignment(fn, name, type);

        int timeToDo = RANDOM.nextInt(MAX_ASSIGNMENT_TIME);

        try {
            Thread.sleep(timeToDo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        studentGradingAPI.submitAssignment(assignment);
    }

    public int getFn() {
        return fn;
    }

    public String getName() {
        return name;
    }

    public StudentGradingAPI getGrader() {
        return studentGradingAPI;
    }

}