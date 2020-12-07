package sequence.converter;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sequence.model.Task;
import sequence.model.TaskDto;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class TaskConverterImpl implements TaskConverter {

    @Override
    public TaskDto entityToTaskId(Task task) {
        TaskDto taskDto = new TaskDto();
        if (task != null) {
            taskDto.setTaskUuid(task.getTaskUuid());
        }
        return taskDto;
    }

    @Override
    public TaskDto entityToResultStatus(Task task) {
        TaskDto taskDto = new TaskDto();
        if (task != null) {
            taskDto.setStatus(task.getStatus());
        }
        return taskDto;
    }

    @Override
    public TaskDto entityToResultSequence(Task task) {
        TaskDto taskDto = new TaskDto();
        if (task != null && !CollectionUtils.isEmpty(task.getResults())) {
            List<String> sequences = task.getResults().stream()
                    .map(result -> result.getSequence()).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sequences)) {
                return taskDto;
            }
            if (sequences.size() == 1) {
                taskDto.setResult(sequences.get(0));
            } else {
                taskDto.setResults(sequences);
            }
        }
        return taskDto;
    }
}
