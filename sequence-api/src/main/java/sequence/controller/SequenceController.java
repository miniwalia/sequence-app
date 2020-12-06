package sequence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sequence.Actions;
import sequence.converter.TaskConverter;
import sequence.converter.TaskConverterImpl;
import sequence.model.SequenceRequest;
import sequence.model.Task;
import sequence.service.SequenceService;

import java.util.*;

/* Sequence Controller */

@RestController
@RequestMapping("/api")
public class SequenceController {
    @Autowired
    public SequenceService service;
    @Autowired
    public TaskConverter converter;

    /** Creates a task In Progress state.
    * Initiate sequence calculation asynchronously.
    * @param request Input to calculate Sequence
    * @return UUID for new task
    * */
    @PostMapping("/generate")
    public ResponseEntity<Object> generateSequence(@RequestBody SequenceRequest request) {
        Task task = this.service.generateSequence(request);
         return new ResponseEntity<>(converter.entityToTaskId(task), HttpStatus.ACCEPTED);
    }

    /** Creates a task In Progress state.
     * Initiate sequences calculation asynchronously
     * @param request Inputs to calculate Sequences
     * @return UUID for new task
     * */
    @PostMapping("/bulkGenerate")
    public ResponseEntity<Object> generateBulkSequences(@RequestBody List<SequenceRequest> request) {
        Task task = this.service.generateBulkSequences(request);
        return new ResponseEntity<>(converter.entityToTaskId(task), HttpStatus.ACCEPTED);
    }

    /** Return status of existing task by its UUID
     * @param id UUID of created task
     * @return Task Status.Possible Values: In Progress, Error, Success
     * */
    @GetMapping("/{id}/status")
    public ResponseEntity<Object> getStatus(@PathVariable UUID id) {
        Task task = this.service.getTask(id);
        return new ResponseEntity<>(converter.entityToResultStatus(task), HttpStatus.ACCEPTED);
    }

    /** Return Generated sequences for given task if action is get_numlist.
     * @param id UUID of created task
     * @param action Returns response on basis of action.
     *              Currently supported action is get_numList.
     * @return Generated Sequences.
     * */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Object> getNumbers(@PathVariable UUID id, @RequestParam String action) {
        Task task = null;
        Actions actionVal = Actions.getByAction(action);
        switch (actionVal) {
            case GetNumbers:
                task = this.service.getTask(id);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(converter.entityToResultSequence(task), HttpStatus.ACCEPTED);
    }
}
