package sequence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
/** Task Entity.
* Contains Current Status of task
* List of results generation during task execution
* */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(updatable = false)
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty(value="task")
    private UUID taskUuid;
    @JsonProperty(value="result")
    private TaskStatus status;
    @ManyToMany
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    @JsonProperty(value="results")
    private Set<Result> results;

    public Task() {
    }

    public Task(TaskStatus status) {
        this.status = status;
    }

    public Set<Result> getResults() {
        return results;
    }

    public void setResult(Set<Result> results) {
        this.results = results;
    }

    public UUID getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(UUID taskUuid) {
        taskUuid = taskUuid;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", results=" + results +
                '}';
    }
}
