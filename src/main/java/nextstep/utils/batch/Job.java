package nextstep.utils.batch;

import java.util.List;

public class Job {

    private List<Step> steps;

    private Job(List<Step> steps) {
        this.steps = steps;
    }

    public static JobBuilder builder() {
        return new JobBuilder();
    }

    public Job addStep(Step step) {
        steps.add(step);
        return this;
    }

    public void run() {

        steps.forEach(Step::start);
    }

    public static class JobBuilder {
        private List<Step> steps;

        public JobBuilder step(Step step) {
            steps.add(step);
            return this;
        }

        public Job build() {
            return new Job(steps);
        }
    }

}
