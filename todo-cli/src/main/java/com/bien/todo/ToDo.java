package com.bien.todo;

import java.util.ArrayList;


public class ToDo {
    private ArrayList<Task> tasks = new ArrayList<>();

    public void add(Task task) {
        if (this.tasks.contains(task)) {
            return;
        }
        tasks.add(task);
    }
    public void remove(Task task) {
       this.tasks.remove(task);
    }
    public void setComplete(Task.taskStatus taskStatus) {
        taskStatus = taskStatus.Completed;
    }
    public void setInProgress(Task.taskStatus taskStatus) {
        taskStatus = taskStatus.InProgress;
    }
    public void printTasks(){
        for (Task task : this.tasks) {
            System.out.println(task);
        }
    }

}
