package sequence.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sequence.Actions;
import sequence.model.SequenceRequest;
import sequence.model.TaskDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@ActiveProfiles("non-async-test")
public class SequenceControllerNonAsyncTest extends AbstractSeqTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetNumAction_shouldReturnResultWithNoSequence() throws Exception {
        MvcResult numListResult = this.getNumList(UUID.randomUUID(), Actions.GetNumbers);
        String numListContent = numListResult.getResponse().getContentAsString();
        TaskDto response = super.mapFromJson(numListContent, TaskDto.class);
        assertTrue(CollectionUtils.isEmpty(response.getResults()));
        assertNull(response.getResult());
    }

    @Test
    public void testGetNumAction_shouldReturnResultWithSingleSequence() throws Exception {
        String inputJson = super.mapToJson(new SequenceRequest(100, 10));
        MvcResult seqResult = this.getSequence(SINGLE_SEQ_GENERATE_API, inputJson);
        String seqContent = seqResult.getResponse().getContentAsString();
        TaskDto taskResponse = super.mapFromJson(seqContent, TaskDto.class);

        MvcResult numListResult = this.getNumList(taskResponse.getTaskUuid(), Actions.GetNumbers);
        String numListContent = numListResult.getResponse().getContentAsString();
        TaskDto response = super.mapFromJson(numListContent, TaskDto.class);
        assertNotNull(response.getResult());
        String expectedSeq = new String("100,90,80,70,60,50,40,30,20,10,0");
        assertEquals(expectedSeq, response.getResult());
    }

    @Test
    public void testGetNumAction_shouldReturnResultWithMultipleSequences() throws Exception {
        MvcResult seqResult = this.getSequence(BULK_GENERATE_API, super.mapToJson(BULK_SEQUENCE_REQUEST));
        String seqContent = seqResult.getResponse().getContentAsString();
        TaskDto taskResponse = super.mapFromJson(seqContent, TaskDto.class);

        MvcResult numListResult = this.getNumList(taskResponse.getTaskUuid(), Actions.GetNumbers);
        String numListContent = numListResult.getResponse().getContentAsString();
        TaskDto response = super.mapFromJson(numListContent, TaskDto.class);
        assertTrue(response.getResults().size() > 1);
        List<String> expectedBulkSeq = new ArrayList(){{
                    add(new String("20,15,10,5,0"));
                    add(new String("10,8,6,4,2,0"));
                }};
        for (String seq : expectedBulkSeq) {
            assertTrue(response.getResults().contains(seq));
        }
    }

    @Test
    public void testGetDefaultAction_shouldNotReturnResult() throws Exception {
        MvcResult numListResult = this.getNumList(UUID.randomUUID(), null);
        String numListContent = numListResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isEmpty(numListContent));
    }

    private MvcResult getNumList(UUID uuid, Actions action) throws Exception {
        String actionName = null;
        if (action != null) {
            actionName = action.getAction();
        }
        return mvc.perform(MockMvcRequestBuilders.get(GET_NUM_LIST_API, uuid)
                .param("action", actionName)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }
}
