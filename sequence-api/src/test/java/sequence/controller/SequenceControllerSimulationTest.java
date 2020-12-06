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
public class SequenceControllerSimulationTest extends AbstractSeqTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    private static final List<SequenceRequest> BULK_SEQUENCE_REQUEST = new ArrayList<SequenceRequest>(){
        {
            add(new SequenceRequest(2000000, 5));
            add(new SequenceRequest(10, 4));
        }
    };

    @Test
    public void testGetNumAction_shouldReturnResultWithMultipleSequences() throws Exception {
        MvcResult seqResult = this.getSequence(BULK_GENERATE_API, super.mapToJson(BULK_SEQUENCE_REQUEST));
        String seqContent = seqResult.getResponse().getContentAsString();
        TaskDto taskResponse = super.mapFromJson(seqContent, TaskDto.class);

        MvcResult numListResult = this.getNumList(taskResponse.getTaskUuid(), Actions.GetNumbers);
        String numListContent = numListResult.getResponse().getContentAsString();
        TaskDto response = super.mapFromJson(numListContent, TaskDto.class);
        assertTrue(response.getResults().size() > 1);
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
