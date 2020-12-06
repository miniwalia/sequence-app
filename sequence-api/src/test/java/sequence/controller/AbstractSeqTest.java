package sequence.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sequence.SequenceApp;
import sequence.model.SequenceRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SequenceApp.class)
@WebAppConfiguration
public abstract class AbstractSeqTest {
    protected static final List<SequenceRequest> BULK_SEQUENCE_REQUEST = new ArrayList<SequenceRequest>();
    protected static final String SINGLE_SEQ_GENERATE_API = "/api/generate";
    protected static final String BULK_GENERATE_API = "/api/bulkGenerate";
    protected static final String GET_NUM_LIST_API = "/api/tasks/{id}";

    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).
                build();
        BULK_SEQUENCE_REQUEST.add(new SequenceRequest(20, 5));
        BULK_SEQUENCE_REQUEST.add(new SequenceRequest(10, 2));
    }
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
    protected MvcResult getSequence(String uri, String inputJson) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();
    }

}