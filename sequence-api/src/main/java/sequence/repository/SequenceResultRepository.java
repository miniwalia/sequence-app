package sequence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sequence.model.Result;
import sequence.model.SequenceRequest;
import sequence.model.Task;

import java.util.UUID;

@Repository
public interface SequenceResultRepository extends JpaRepository<Result, UUID> {
    Result findByRequest(SequenceRequest request);
}
