package sequence.service.calc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sequence.model.Result;
import sequence.model.SequenceRequest;
import sequence.model.Task;
import sequence.model.TaskStatus;
import sequence.repository.SequenceRepository;
import sequence.repository.SequenceResultRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import static org.aspectj.bridge.MessageUtil.fail;

@Service
public class SequenceCalculateService {
    @Autowired
    private SequenceRepository repository;
    @Autowired
    private SequenceResultRepository resultRepository;
    @Value("${app.sleep.seconds}")
    private int waitSeconds;
    private static final Logger LOG = LoggerFactory.getLogger(SequenceCalculateService.class);

    /** Calculate the list of requests.
    * For each request, performs below action
    * Save task status to error if request is invalid.
     * @see #getStatus(SequenceRequest request)
    * If request is valid, calculate sequence and add to result
     * @see #getResult(SequenceRequest request)
    * Save task status to In Progress and results.
    * */
    @Async("asyncExecutor")
    public Future<String> calculate(
            final List<SequenceRequest> requests,
            final Task task) {
        Instant start = Instant.now();
        if (waitSeconds > 0) {
            this.sleep();
        }
        TaskStatus status = null;
        Set<Result> results = new HashSet<Result>();
        for (SequenceRequest request : requests) {
            status = this.getStatus(request);
            if (status.equals(TaskStatus.Error)) {
                task.setStatus(status);
                results.clear();
                break;
            }
            results.add(this.getResult(request));
        }
        if (status.equals(TaskStatus.Success)) {
            task.setStatus(status);
            this.saveResults(results, task);
        }
        this.repository.save(task);
        LOG.info("calculate method took:{} seconds",
                Duration.between(start, Instant.now()).getSeconds());
        return null;
    }

    protected void saveResults(Set<Result> results, Task task) {
        try {
            for (Result result : results) {
                this.resultRepository.save(result);
            }
            task.setResult(results);
        } catch (Exception e) {
            task.setStatus(TaskStatus.Error);
        }
    }

    /** Return task status as error if
     *  Request goal or step is less than or equal to zero or goal is less than step
     * else return In Progress
     * @param request Sequence Request
     * @return TaskStatus Error or In Progress
     * */
    protected TaskStatus getStatus(final SequenceRequest request) {
        TaskStatus status = TaskStatus.Success;
        if (request.getGoal() <=0
                || request.getGoal() < request.getStep()
                || request.getStep() <= 0) {
            status = TaskStatus.Error;
        }
        return status;
    }

    /** Return existing result id calculated as part of other task
     * or create new result and generate sequence.
     * @see #generateSeq(int goal, int step)
     * @param request Sequence request for which sequence to be generated
     * @return Result containing request and generated sequence
     * */
    protected Result getResult(SequenceRequest request) {
        Result result = this.getExistingResult(request);
        if (result == null) {
            result = new Result(request);
            List<Integer> sequence =  this.generateSeq(request.getGoal(), request.getStep());
            result.setSequence(StringUtils.collectionToDelimitedString(sequence, ","));
        }
        return result;
    }

    /** Returns existing result if saved.
     * @param request Input request for which result to be fetched.
     * @return Result Existing result
     */
    public Result getExistingResult(SequenceRequest request) {
        return this.resultRepository.findByRequest(request);
    }

    /** Calculate sequence from goal to 0 with a difference of given step.
     * @param goal Initial number for sequence calculation
     * @param step Difference to be calculated between two numbers in a sequence
     * @return List of integers
     * */
    protected List<Integer> generateSeq(int goal, int step) {
        List<Integer> seqs = new ArrayList<>();
        seqs.add(goal);
        int diff = goal - step;
        while(diff >=0) {
            seqs.add(diff);
            diff = diff - step;
        }
        return seqs;
    }

    private void sleep() {
        try {
            Thread.sleep(waitSeconds* 1000);
        } catch (InterruptedException e) {
            fail("Erro while executing thread");

        }
    }
}
