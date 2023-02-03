package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;

import java.util.concurrent.atomic.AtomicInteger;

public class Assistant extends Thread {

    private final AtomicInteger gradedAssignmentsCount = new AtomicInteger();
    private final String name;
    private final AdminGradingAPI grader;

    public Assistant(String name, AdminGradingAPI grader) {
        this.name = name;
        this.grader = grader;
    }

    @Override
    public void run() {
        Assignment assignment = grader.getAssignment();
        while (assignment != null) {
            synchronized (this) {
                try {
                    wait(assignment.type().getGradingTime());
                    gradedAssignmentsCount.incrementAndGet();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            assignment = grader.getAssignment();
        }

    }

    public int getNumberOfGradedAssignments() {
        return gradedAssignmentsCount.get();
    }

    public String getAssistantName() {
        return name;
    }

}