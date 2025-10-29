package com.bien.todo;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ToDo {
    private ArrayList<Task> tasks;
    private static final String DEFAULT_SAVE_FILE = "tasks.dat";

    public ToDo() {
        this.tasks = new ArrayList<>();
    }

    //=========== Add/Remove Operations =============
    public boolean add(Task task) {
        if (task == null) {
            System.out.println("Cannot add null task");
            return false;
        }
        if (tasks.contains(task)) {
            System.out.println("Task already exists");
            return false;
        }
        this.tasks.add(task);
        return true;
    }

    public boolean remove(Task task) {
        return tasks.remove(task);
    }

    public boolean removeByIndex(int index) {
        if (!isValidIndex(index)) {
            System.out.println("Invalid task number");
            return false;
        }
        tasks.remove(index);
        return true;
    }

    public boolean removeById(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    public void clearCompletedTasks() {
        long removed = tasks.stream().filter(Task::isCompleted).count();
        tasks.removeIf(Task::isCompleted);
        System.out.println("Removed " + removed + "completed tasks");
    }

    public void clearAll() {
        tasks.clear();

    }
     //=============Retrieval Operation================
    public ArrayList<Task> getCompletedTasks() {
        return tasks.stream().filter(Task::isCompleted)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public Task getTask(int index){
        return isValidIndex(index) ? tasks.get(index) : null;
    }
    public Task getTaskById(int id){
        return tasks.stream().filter(t->t.getId() == id).findFirst().orElse(null);
    }
    public ArrayList<Task> getTasks(){
        return new ArrayList<>(tasks);
    }
    public ArrayList<Task> searchTasks(String keyword){
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Task> result = new ArrayList<>();
        String lowerCaseKeyword = keyword.toLowerCase();
        return tasks.stream().filter(t->t.getName().toLowerCase().contains(lowerCaseKeyword)||
                t.getDescription().toLowerCase().contains(lowerCaseKeyword)).collect(Collectors
                .toCollection(ArrayList::new));
    }
    public ArrayList<Task> getTasksByStatus(Task.TaskStatus status){
        return tasks.stream().filter(t->t.getStatus()==status)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getTasksByPriority(Task.Priority priority){
        return tasks.stream().filter(t->t.getPriority()==priority)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getOverdueTasks(){
        return tasks.stream().filter(Task::isOverdue)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getTasksDueToday(){
        return tasks.stream().filter(Task::isDueToday)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getTasksDueSoon(int days){
        return tasks.stream().filter(t->t.isDueSoon(days))
                .collect(Collectors.toCollection(ArrayList::new));
    }

 //===============Sorting Operations======================
    public void sortByPriority(){
        tasks.sort(Comparator.comparing(t->t.getPriority().getLevel()));
    }
    public void sortByStatus(){
        tasks.sort(Comparator.comparing(Task::getStatus));
    }
    public void sortByName(){
        tasks.sort(Comparator.comparing(Task::getName,String.CASE_INSENSITIVE_ORDER));
    }
    public void sortByDueDate(){
         tasks.sort((t1,t2)-> {
             if (t1.getDueDate() == null && t2.getDueDate() == null) return 0;
             if (t1.getDueDate()==null) return 1;
             if (t2.getDueDate()==null) return -1;
             return t1.getDueDate().compareTo(t2.getDueDate());
         });
    }
}
