package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class CodePostGrader implements AdminGradingAPI {

    private boolean inProgress = true;
    private final AtomicInteger assignmentsCount = new AtomicInteger();
    private final Queue<Assignment> assignments = new LinkedList<>();

    private final Assistant[] assistants;

    public CodePostGrader(int numberOfAssistants) {
        assistants = new Assistant[numberOfAssistants];
        for (int i = 0 ; i < numberOfAssistants; ++i) {
            assistants[i] = new Assistant("Assistant " + i, this);
            assistants[i].start();
        }
    }

    @Override
    public synchronized Assignment getAssignment() {

        while (inProgress && assignments.isEmpty() ) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return assignments.poll();
    }

    @Override
    public int getSubmittedAssignmentsCount() {
        return assignmentsCount.get();
    }

    @Override
    public void finalizeGrading() {
        inProgress = false;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public List<Assistant> getAssistants() {
        return Arrays.stream(assistants).toList();
    }

    @Override
    public void submitAssignment(Assignment assignment) {
        if (!inProgress) {
            return;
        }
        synchronized (assignments) {
            assignments.add(assignment);
        }
        synchronized (this) {
            notify();
        }
        assignmentsCount.incrementAndGet();

    }


}
