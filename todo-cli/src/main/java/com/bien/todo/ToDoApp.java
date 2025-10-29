package com.bien.todo;

import java.time.LocalDate;
import java.util.Scanner;


public class ToDoApp {
    private ToDo toDoManager;
    private Scanner scanner;
    private boolean running;

    public ToDoApp(){
        this.toDoManager = new ToDo();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    public void start(){
        //Load existing tasks
        toDoManager.loadFromFile();

        printWelcome();
        toDoManager.printDashBoard();
        while(running){
            printMenu();
            handleUserInput();
        }
        //Autosave on exit
        toDoManager.saveToFile();
        System.out.println("\nGoodbye! Your tasks have been saved.");
        scanner.close();
    }
    public void printWelcome(){
        System.out.printf("Welcome to the To-Do list application!\n");
    }
    public void printMenu(){
        System.out.println("=================TASK MANAGER===============");
        System.out.println("1. Add Task                11. Search Tasks");
        System.out.println("2. View All Tasks          12. Sort Tasks");
        System.out.println("3. View Task Details       13. Statistics");
        System.out.println("4. Edit Task               14. Dashboard");
        System.out.println("5. Delete Task             15. Clear completed");
        System.out.println("6. Mark Complete           16. Clear all");
        System.out.println("7. Mark In Progress        17. Save tasks");
        System.out.println("8. Filter by Status        18. Load tasks");
        System.out.println("9. Filter by Priority      19. Exit");
        System.out.println("10. View overdue");
        System.out.println("============================================");
    }
    private void handleUserInput(){
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (choice){
                case 1: addTask(); break;
                case 2: viewAllTasks(); break;
                case 3: viewTaskDetails(); break;
                case 4: editTask(); break;
                case 5: deleteTask(); break;
                case 6: markComplete(); break;
                case 7: markInProgress(); break;
                case 8: filterByStatus(); break;
                case 9: filterByPriority(); break;
                case 10: viewOverdue(); break;
                case 11: searchTasks(); break;
                case 12: sortTasks(); break;
                case 13: viewStatistics(); break;
                case 14: viewDashBoard(); break;
                case 15: clearCompleted(); break;
                case 16: clearAll(); break;
                case 17: saveTasks(); break;
                case 18: loadTasks(); break;
                case 19: exit();break;
                default:
                    System.out.println("Invalid choice. Please choose 1-19.");
            }
        }
        catch(NumberFormatException e){
            System.out.println("Invalid input.Please choose a number");
        }
    }
    // Task Manager Methods
    public void addTask(){
        System.out.println("====Add new task=====");

        System.out.print("Task name:");
        String name = scanner.nextLine().trim();
        if(name.isEmpty()){
            System.out.println("Task name cannot be empty");
            return;
        }
        System.out.print("Description(optional):");
        String description = scanner.nextLine().trim();

        Task.Priority priority = selectPriority();
        Task.TaskStatus status = selectStatus();

        Task task = new Task(name,description,priority,status);

        System.out.print("Set due date?(y/n)");
        if(scanner.nextLine().trim().equalsIgnoreCase("y")){
            LocalDate dueDate = inputDate();
            if(dueDate != null){
                task.setDueDate(dueDate);
            }
        }
        if(toDoManager.add(task)){
            System.out.println("Task has been added successfully");
            System.out.println(task);
        }
    }

}
