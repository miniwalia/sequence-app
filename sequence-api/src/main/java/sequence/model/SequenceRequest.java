package sequence.model;

import javax.persistence.Embeddable;

/** Sequence Generation Request
* goal: Initial number to start for generating sequence.
* step: Difference between two numbers in sequence.
* */
@Embeddable
public class SequenceRequest {
    private int goal;
    private int step;

    public SequenceRequest() {
    }

    public SequenceRequest(int goal, Integer step) {
        this.goal = goal;
        this.step = step;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
