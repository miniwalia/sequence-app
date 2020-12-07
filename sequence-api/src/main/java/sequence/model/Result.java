package sequence.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/** Result Entity.
 * Contains Sequence Request
 * List of integers representing sequence generated for given input
 * */
@Entity
public class Result {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(updatable = false)
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;
    @Embedded
    private SequenceRequest request;
    @Lob
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private String sequence ;

    public Result() {
    }

    public Result(SequenceRequest request) {
        this.request = request;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        uuid = uuid;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "Result{" +
                "uuid=" + uuid +
                ", request=" + request +
                ", sequence=" + sequence +
                '}';
    }
}
