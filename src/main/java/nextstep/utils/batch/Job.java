package nextstep.utils.batch;

import nextstep.utils.batch.step.Step;

import java.util.ArrayList;
import java.util.List;

public class Job {

    private List<Step> steps;

    private Job(List<Step> steps) {
        this.steps = steps;
    }

    public static JobBuilder builder() {
        return new JobBuilder();
    }

    public void run() {
        steps.forEach(Step::start);
    }

    public void runWithCallback(Runnable runnable) {
        run();
        runnable.run();
    }

    public static class JobBuilder {
        private List<Step> steps = new ArrayList<>();

        public JobBuilder step(Step step) {
            steps.add(step);
            return this;
        }

        public Job build() {
            return new Job(steps);
        }
    }

}
