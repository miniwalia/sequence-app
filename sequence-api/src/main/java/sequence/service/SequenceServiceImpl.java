package sequence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sequence.model.SequenceRequest;
import sequence.model.Task;
import sequence.model.TaskStatus;
import sequence.repository.SequenceRepository;
import sequence.service.calc.SequenceCalculateService;

import java.util.*;
@Service
@Profile({"!non-async-test"})
public class SequenceServiceImpl implements SequenceService{
    @Autowired
    protected SequenceRepository repository;
    @Autowired
    protected SequenceCalculateService calcService;

    public Task generateSequence(final SequenceRequest seqGenRequest) {
        return this.createTask(Arrays.asList(seqGenRequest));
    }

    public Task generateBulkSequences(final List<SequenceRequest> seqGenRequests) {
        return this.createTask(seqGenRequests);
    }

    public Task getTask(final UUID uuid) {
        return this.repository.findOne(uuid);
    }


    private Task createTask(final List<SequenceRequest> seqGenRequests) {
        Task task = new Task(TaskStatus.InProgress);
        this.repository.save(task);
        this.calculate(seqGenRequests, task);
        return task;
    }

    public void calculate(
            final List<SequenceRequest> seqGenRequests,
            final Task task) {
        this.calcService.calculate(seqGenRequests, task);
    }
}
