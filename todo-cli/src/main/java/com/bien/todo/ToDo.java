package com.bien.todo;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLOutput;
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
    public void sortByCreatedDate(){
        tasks.sort(Comparator.comparing(Task::getCreatedAt));
    }
    //======Display Operations ===========
    public void printTasks(ArrayList<Task> tasks,String title){
        System.out.println(title + " ("+tasks.size()+")");
        if (tasks.isEmpty()) System.out.println("No tasks found");
        for (int i = 0; i<tasks.size(); i++){
            System.out.println(i+". "+tasks.get(i));
        }
    }

    public void printTasks() {
        printTasks(tasks,"All tasks");
    }
    public void printTasksByStatus(Task.TaskStatus status){
        ArrayList<Task> filtered = getTasksByStatus(status);
        printTasks(filtered,"Tasks by status: "+status);
    }
    public void printTasksByPriority(Task.Priority priority){
        ArrayList<Task> filtered = getTasksByPriority(priority);
        printTasks(filtered,"Tasks by priority: "+priority);
    }
    public void printOverdueTasks(){
        ArrayList<Task> overdue = getTasksDueToday();
        printTasks(overdue,"Overdue tasks");
    }
    public void printTasksDueToday(){
        ArrayList<Task> tasks = getTasksDueToday();
        printTasks(tasks,"Tasks due today");
    }
    public void printStatistics(){
        int total = tasks.size();
        long completed = tasks.stream().filter(Task::isCompleted).count();
        long inProgress = total - completed;
        long overdue =tasks.stream().filter(Task::isOverdue).count();
        System.out.println("Statistics:");
        System.out.println("Total tasks: "+total);
        System.out.println("Completed tasks: "+completed);
        System.out.println("InProgress tasks: "+inProgress);
        System.out.println("Overdue tasks: "+overdue);
        if (total>0){
            double completetionRate = (completed*100.0)/total;
            System.out.println("Completion rate: "+completetionRate+"%");
        }

    }
    public void printDashBoard(){
        System.out.println("Task Dashboard");
        //Tasks due today
        ArrayList<Task> dueToday = getTasksDueToday();
        if (!dueToday.isEmpty()){
            System.out.println("Tasks due today :");
            for (Task task : dueToday){
                System.out.println(task);
            }
        }
        //Overdue
        ArrayList<Task> overdue = getOverdueTasks();
        if(!overdue.isEmpty()){
            System.out.println("Overdue tasks :");
            for (Task task : overdue){
                System.out.println(task);
            }
        }
        //Due soon ( 3 days)
        ArrayList<Task> dueSoon = getTasksDueSoon(3);
        if(!dueSoon.isEmpty()){
            System.out.println("Tasks due soon in the next 3 days :");
            for (Task task : dueSoon){
                System.out.println(task);
            }
        }
        //Urgent tasks
        ArrayList<Task> urgent = getTasksByPriority(Task.Priority.URGENT);
        if(!urgent.isEmpty()){
            System.out.println(" Urgent tasks :");
            for (Task task : urgent.stream().filter(t->!t.isCompleted()).collect(Collectors.toList())){
                System.out.println(task);
            }
        }
        if (dueToday.isEmpty() && overdue.isEmpty() && dueSoon.isEmpty() && urgent.isEmpty()) {
            System.out.println("\n✅ All caught up! No urgent tasks.");
        }

    }
    //========Persistence Operations===============
    public void saveToFile(String fileName){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            oos.writeObject(tasks);
            System.out.println("Tasks saved successfully to "+ fileName);
         } catch (IOException e){
            System.out.println("Error saving file"+ e.getMessage());
        }
    }
    public void saveToFile(){
        saveToFile(DEFAULT_SAVE_FILE);
    }
}
