package sequence.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sequence.model.SequenceRequest;
import sequence.model.TaskDto;
import sequence.model.TaskStatus;
import sequence.service.SequenceService;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class SequenceControllerSkipAsyncTasksTest extends AbstractSeqTest {
    @Mock
    private SequenceService service;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateAndStatus_shouldCreateTaskInProgress() throws Exception {
        String inputJson = super.mapToJson(new SequenceRequest(100, 10));
        doNothing().when(service).calculate(anyList(), anyObject());
        MvcResult getSeqResult = this.getSequence(SINGLE_SEQ_GENERATE_API, inputJson);
        String seqContent = getSeqResult.getResponse().getContentAsString();
        TaskDto taskResponse = super.mapFromJson(seqContent, TaskDto.class);
        assertNotNull(taskResponse.getTaskUuid());
        this.testGetStatus_shouldReturnInProgress(taskResponse);
    }

    @Test
    public void testBulkGenerateAndStatus_shouldCreateTaskInProgress() throws Exception {
        doNothing().when(service).calculate(anyList(), anyObject());
        MvcResult getSeqResult = this.getSequence(BULK_GENERATE_API, super.mapToJson(BULK_SEQUENCE_REQUEST));
        String seqContent = getSeqResult.getResponse().getContentAsString();
        TaskDto taskResponse = super.mapFromJson(seqContent, TaskDto.class);
        assertNotNull(taskResponse.getTaskUuid());
        this.testGetStatus_shouldReturnInProgress(taskResponse);

    }

    private MvcResult getStatus(String uri, UUID uuid) throws Exception{
        return mvc.perform(MockMvcRequestBuilders.get(uri, uuid)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();
    }

    private void testGetStatus_shouldReturnInProgress(TaskDto response) throws Exception {
        MvcResult statusResult = this.getStatus("/api/{id}/status", response.getTaskUuid());
        String statusContent = statusResult.getResponse().getContentAsString();
        TaskDto statusResponse = super.mapFromJson(statusContent, TaskDto.class);
        assertEquals(TaskStatus.InProgress, statusResponse.getStatus());
    }
}
