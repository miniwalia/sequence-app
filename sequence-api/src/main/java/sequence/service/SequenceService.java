package sequence.service;

import sequence.model.SequenceRequest;
import sequence.model.Task;

import java.util.List;
import java.util.UUID;

public interface SequenceService {

    Task generateSequence(SequenceRequest seqGenRequest);

    Task generateBulkSequences(final List<SequenceRequest> seqGenRequests);

    Task getTask (UUID uuid);

    void calculate(List<SequenceRequest> seqGenRequests, Task task);
}
