package prahimProject.demo.model;

public enum TaskStatus {
    PENDING, //initial state when a task is created
    ASSIGNED, //task is assigned to a worker but not yet started
    PROCESSING, //task is currently being worked on
    DONE,//task is completed successfully
    FAILED,//task failed to complete successfully
    CANCELLED//task was cancelled by the user or system
}
