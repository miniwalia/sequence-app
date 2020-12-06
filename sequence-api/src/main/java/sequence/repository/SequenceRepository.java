package sequence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sequence.model.Task;

import java.util.UUID;

@Repository
public interface SequenceRepository extends JpaRepository<Task, UUID> {

}
