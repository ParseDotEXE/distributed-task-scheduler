package prahimProject.demo.model;
import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Task {
    //private fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private int priority;

    //default constructor -> always needed for JPA
    public Task() {
        this.status = TaskStatus.PENDING; //default status
    }
    //parameterized constructor
    public Task(String name, String description, LocalDateTime dueDate, int priority) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = TaskStatus.PENDING; //default status
    }
    //getters and setters
    public Long getId() {
        return id;
    }
    //might be needed for JPA, but not used in the code
    protected void setId(Long id){
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    //methods
    public void markAsDone(){
        this.status = TaskStatus.DONE;
    }
    public boolean assignTask(){
        if(this.status == TaskStatus.PENDING){
            this.status = TaskStatus.ASSIGNED;
            return true; //task assigned successfully
        } else {
            return false; //task is already assigned or done
        }
    }
}
