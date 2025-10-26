package com.bien.todo;


import java.util.ArrayList;

public class ToDo {
    private ArrayList<Task> tasks;
    private static final String DEFAULT_SAVE_FILE = "tasks.dat";

    public ToDo(){
        this.tasks = new ArrayList<>();
    }
    //=========== Add/Remove Operations =============
    public boolean add(Task task){
        if (task==null){
            System.out.println("Cannot add null task");
            return false;
        }
        if (tasks.contains(task)){
            System.out.println("Task already exists");
            return false;
        }
        this.tasks.add(task);
        return true;
    }
    public boolean remove(Task task){
        return tasks.remove(task);
    }
    public boolean removeByIndex(int index){
        if(!isValidIndex(index)){
            System.out.println("Invalid task number");
            return false;
        }
        tasks.remove(index);
        return true;
    }
    public boolean removeById(int id){
        return tasks.removeIf(t->t.getId()==id);
    }
    public void clearCompletedTasks(){
        long removed = tasks.stream().filter(Task::isCompleted).count();
        tasks.removeIf(Task::isCompleted);
        System.out.println("Removed " + removed + "completed tasks");
    }

}
