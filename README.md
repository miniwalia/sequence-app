# sequence-app

This project contains a spring boot application that generates a sequence of numbers in the decreasing order till 0.

## EndPoints 
 ### \<server:8080>/api/generate
- Creates a task in "In Progress" state.Initiate sequence calculation asynchronously.Returns UUID of task
### \<server:8080>/api/bulkGenerate 
- Creates a task in "In Progress" state for multiple input sequences.Initiate sequence calculation asynchronously.Returns UUID of task
### \<server:8080>/api/{id}/status
- Return status of existing task by its UUID.Possible Values: In Progress, Error, Success
### \<server:8080>/tasks/{id}?action=get_numlist
- Return Generated sequences for given task UUID.

## Tests
### SequenceControllerSimulationTest 
 - Calls "/api/generate" endpoint to create task and initaite async call to calculate sequence.
 - Wait for Async task to complete(Given input takes more than 20 seconds).
 - Calls "/tasks/{id}?action=get_numlist" to verify result is generated.
### SequenceControllerNonAsyncTest 
- Test class for generating and getting generated sequence.
### SequenceControllerSkipAsyncTasksTest 
- Test class for generating task in IN_PROGRESS status.
