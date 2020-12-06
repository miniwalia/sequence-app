package sequence.converter;

import sequence.model.Task;
import sequence.model.TaskDto;

/**
 * Converts Task Entity to Response.
 */
public interface TaskConverter {
    /**
     * Populates UUID from Task Entity.
     * @param task Task Entity
     * @return TaskDto with UUID
     */
    TaskDto entityToTaskId(Task task);

    /**
     * Populates status from Task Entity.
     * @param task Task Entity
     * @return TaskDto with status
     */
    TaskDto entityToResultStatus(Task task);

    /**
     * Populates list of sequences from Task Entity.
     * @param task Task Entity
     * @return TaskDto with list of sequences
     */
    TaskDto entityToResultSequence(Task task);
}
