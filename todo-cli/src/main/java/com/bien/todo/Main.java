package com.bien.todo;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ToDo tasks = new ToDo();
        tasks.add(new Task("Washing Dishes","Washing dishes in the sink", Task.taskStatus.InProgress, Task.Priority.LOW));
        Task doHomeWork = new Task("Do homework","Solve DSA+SQL problems", Task.taskStatus.InProgress, Task.Priority.URGENT);
        doHomeWork.setHigh();
        tasks.add(doHomeWork);
        tasks.add(new Task("Playing Marvel Rivals","Derank to Diamond", Task.taskStatus.InProgress, Task.Priority.URGENT));
        tasks.remove(doHomeWork);
        tasks.printTasks();
    }
}