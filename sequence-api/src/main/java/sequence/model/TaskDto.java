package sequence.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Task Dto.
* */
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class TaskDto {
    @JsonProperty(value = "task")
    private UUID taskUuid;
    @JsonProperty(value = "status")
    private TaskStatus status;
    @JsonProperty(value = "result")
    private String result;
    @JsonProperty(value = "results")
    private List<String> results;

    public TaskDto() {
    }

    public UUID getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(UUID taskUuid) {
        this.taskUuid = taskUuid;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
