package com.bien.todo;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

public class Task implements Serializable, Comparable<Task> {
    private static final long serialVersionUID = 1L;
    private static int idCounter =0;

    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate dueDate;

    public enum TaskStatus{
        InProgress,
        Completed,
    }
    public enum Priority{
        URGENT(1),
        HIGH(2),
        MEDIUM(3),
        LOW(4);

        private final int level;
        Priority(int level) {
            this.level = level;
        }
        public int getLevel() {
            return level;
        }
    }

    public Task(String name, String description,TaskStatus status,Priority priority) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }
        this.id = idCounter++;
        this.name = name.trim();
        this.description = description != null ? description.trim() :"";
        this.status = status != null? status : TaskStatus.InProgress;
        this.priority = priority != null? priority:Priority.MEDIUM;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }
    public Task(String name, String description){
        this(name,description,TaskStatus.InProgress,Priority.MEDIUM);
    }
    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Priority getPriority() {
        return priority;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }
        this.name = name.trim();
        this.updatedAt = LocalDateTime.now();
    }
    public void setDescription(String description) {
        this.description = description != null ? description.trim() :"";
        this.updatedAt = LocalDateTime.now();
    }
    public void setPriority(Priority priority) {
        this.priority = priority != null?priority:Priority.MEDIUM;
        this.updatedAt = LocalDateTime.now();
    }
    public void setStatus(TaskStatus status) {
        this.status = status != null? status : TaskStatus.InProgress;
        this.updatedAt = LocalDateTime.now();
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }
    public void setCompleted(){
        this.status = TaskStatus.Completed;
        this.updatedAt = LocalDateTime.now();
    }
    public void setInProgress(){
        this.status = TaskStatus.InProgress;
        this.updatedAt = LocalDateTime.now();
    }
    public boolean isCompleted(){
        return this.status == TaskStatus.Completed;
    }
    public boolean isOverdue(){
        return dueDate !=null &&
                LocalDate.now().isAfter(dueDate)
                && status != TaskStatus.Completed;
    }
    public boolean isDueToday(){
        return dueDate != null && dueDate.equals(LocalDateTime.now());
    }
    public boolean isDueSoon(int days) {
        if (dueDate == null || isCompleted()) return false;
        LocalDate today = LocalDate.now();
        return !today.isAfter(dueDate) &&
                today.plusDays(days).isAfter(dueDate);
    }

    @Override
    public int compareTo(Task o) {
        int priorityCompare = Integer.compare(this.getPriority().getLevel(),
                o.getPriority().getLevel());
        return priorityCompare !=0 ? priorityCompare: this.status.compareTo(o.status);
    }
    @Override
    public boolean equals(Object o ){
        if (this == o) return true;
        if (o==null || !(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(id).append("]");
        sb.append(name);
        if (dueDate != null){
            sb.append(" (Due: ").append(dueDate).append(")");
        }
        sb.append(" [").append(priority).append("]");
        sb.append(" [").append(status).append("]");
        return sb.toString();
    }
    public static void resetIdCounter(){
        idCounter = 0;
    }

}
