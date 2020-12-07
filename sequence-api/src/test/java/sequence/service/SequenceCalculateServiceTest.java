package sequence.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import sequence.model.Result;
import sequence.model.SequenceRequest;
import sequence.model.Task;
import sequence.model.TaskStatus;
import sequence.repository.SequenceRepository;
import sequence.repository.SequenceResultRepository;
import sequence.service.calc.SequenceCalculateService;


import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;

@RunWith(MockitoJUnitRunner.class)
public class SequenceCalculateServiceTest {
    private Task task = new Task();
    @Mock
    private SequenceRepository repository;
    @Mock
    private SequenceResultRepository resultRepository;
    @InjectMocks
    @Spy
    private SequenceCalculateService seqCalcService;
    private static final List<SequenceRequest> BULK_SEQUENCE_REQUEST = new ArrayList<SequenceRequest>(){
        {
            add(new SequenceRequest(20, 5));
            add(new SequenceRequest(10, 4));
        }
    };

    @Test
    public void testCalculate_shouldGenerateNewSequences() {
        /*List<String> expectedBulkSeq = new ArrayList(){{
            add(Arrays.asList("20,15,10,5,0"));
            add(Arrays.asList("10, 6, 2"));
        }};*/

        List<String> expectedBulkSeq = new ArrayList(){{
            add("20,15,10,5,0");
            add("10, 6, 2");
        }};

        Mockito.when(seqCalcService.getExistingResult(anyObject())).thenReturn(null);
        seqCalcService.calculate(BULK_SEQUENCE_REQUEST, task);

        Set<Result> results = task.getResults();
        assertEquals(2, results.size());
        List<String> seqs = results.stream().map(result -> result.getSequence()).collect(Collectors.toList());
        for (String sequence : expectedBulkSeq) {
            seqs.contains(sequence);
        }
    }

    @Test
    public void testCalculate_shouldSetTaskStatusErrorWhenGoalIsZero() {
        Mockito.when(seqCalcService.getExistingResult(anyObject())).thenReturn(null);
        seqCalcService.calculate(Arrays.asList(new SequenceRequest(0, 5)), task);
        this.verifyErrorTaskStatus();
    }
    @Test
    public void testCalculate_shouldSetTaskStatusErrorWhenStepIsZero() {
        Mockito.when(seqCalcService.getExistingResult(anyObject())).thenReturn(null);
        seqCalcService.calculate(Arrays.asList(new SequenceRequest(1, 0)), task);
        this.verifyErrorTaskStatus();
    }
    @Test
    public void testCalculate_shouldSetTaskStatusErrorWhenGoalIsLessThanStep() {
        Mockito.when(seqCalcService.getExistingResult(anyObject())).thenReturn(null);
        seqCalcService.calculate(Arrays.asList(new SequenceRequest(1, 5)), task);
        this.verifyErrorTaskStatus();
    }

    protected void verifyErrorTaskStatus() {
        Set<Result> results = task.getResults();
        assertNull(task.getResults());
        assertEquals(TaskStatus.Error, task.getStatus());
    }
}
