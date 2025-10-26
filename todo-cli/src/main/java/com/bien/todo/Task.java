package com.bien.todo;
public class Task {
    private String name;
    private String description;
    private taskStatus status;
    private Priority priority;
    public enum taskStatus{
        InProgress,
        Completed,
    }
    public enum Priority{
        URGENT,
        HIGH,
        MEDIUM,
        LOW,
    }

    public Task(String name, String description,taskStatus status,Priority priority) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority = priority;

    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Priority getPriority() {
        return this.priority;
    }
    public taskStatus getStatus() {
        return this.status;
    }
    public void setUrgent(){
        Task.Priority priority = Task.Priority.URGENT;
    }
    public void setHigh(){
        Task.Priority priority = Task.Priority.HIGH;
    }
    public void setMedium(){
        Task.Priority priority = Task.Priority.MEDIUM;
    }
    public void setLow(){
        Task.Priority priority = Task.Priority.LOW;
    }
    public void setCompleted(){
        this.status = taskStatus.Completed;
    }
    public void setInProgress(){
        this.status = taskStatus.InProgress;
    }
    @Override
    public String toString() {
        return this.name+" : "+this.description+" "+ this.getPriority().toString()+" "+"("+this.getStatus().toString()+")";
    }

}
