package sequence.model;

public enum TaskStatus {
    Success("SUCCESS"),
    Error("ERROR"),
    InProgress("IN_PROGRESS");

    private String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
