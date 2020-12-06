package sequence.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sequence.model.SequenceRequest;
import sequence.model.Task;

import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.fail;

@Component("testNonAsyncSeqService")
@Profile("non-async-test")
public class TestNonAsyncSeqServiceImpl extends SequenceServiceImpl implements SequenceService {
    @Override
    public void calculate(
            final List<SequenceRequest> seqGenRequests,
            final Task task) {
        Future<String> result = this.calcService.calculate(seqGenRequests, task);
        try {
            result.get();
        } catch (Exception e) {
            fail("Error while executing calc task");
        }
    }

}
