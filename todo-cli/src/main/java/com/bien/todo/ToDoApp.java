package com.bien.todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class ToDoApp {
    private ToDo toDoManager;
    private Scanner scanner;
    private boolean running;

    public ToDoApp() {
        this.toDoManager = new ToDo();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        //Load existing tasks
        toDoManager.loadFromFile();

        printWelcome();
        toDoManager.printDashBoard();
        while (running) {
            printMenu();
            handleUserInput();
        }
        //Autosave on exit
        toDoManager.saveToFile();
        System.out.println("\nGoodbye! Your tasks have been saved.");
        scanner.close();
    }

    public void printWelcome() {
        System.out.printf("Welcome to the To-Do list application!\n");
    }

    public void printMenu() {
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

    private void handleUserInput() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewAllTasks();
                    break;
                case 3:
                    viewTaskDetails();
                    break;
                case 4:
                    editTask();
                    break;
                case 5:
                    deleteTask();
                    break;
                case 6:
                    markComplete();
                    break;
                case 7:
                    markInProgress();
                    break;
                case 8:
                    filterByStatus();
                    break;
                case 9:
                    filterByPriority();
                    break;
                case 10:
                    viewOverdue();
                    break;
                case 11:
                    searchTasks();
                    break;
                case 12:
                    sortTasks();
                    break;
                case 13:
                    viewStatistics();
                    break;
                case 14:
                    viewDashBoard();
                    break;
                case 15:
                    clearCompleted();
                    break;
                case 16:
                    clearAll();
                    break;
                case 17:
                    saveTasks();
                    break;
                case 18:
                    loadTasks();
                    break;
                case 19:
                    exit();
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 1-19.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.Please choose a number");
        }
    }

    // Task Management Methods
    private void addTask() {
        System.out.println("====Add new task=====");

        System.out.print("Task name:");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Task name cannot be empty");
            return;
        }
        System.out.print("Description(optional):");
        String description = scanner.nextLine().trim();

        Task.Priority priority = selectPriority();
        Task.TaskStatus status = selectStatus();

        Task task = new Task(name, description, status, priority);

        System.out.print("Set due date?(y/n)");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            LocalDate dueDate = inputDate();
            if (dueDate != null) {
                task.setDueDate(dueDate);
            }
        }
        if (toDoManager.add(task)) {
            System.out.println("Task has been added successfully");
            System.out.println(task);
        }
    }

    private void viewAllTasks() {
        toDoManager.printTasks();
    }

    private void viewTaskDetails() {
        if (toDoManager.isEmpty()) {
            System.out.println("No tasks available");
        }
        toDoManager.printTasks();
        System.out.print("\nEnter task number to view details: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) ;
            toDoManager.printTaskDetails(index);

        } catch (NumberFormatException e) {
            System.out.println("Invalid task number.");
        }
    }

    private void editTask() {
        if (toDoManager.isEmpty()) {
            System.out.println("No tasks available");
            return;
        }
        toDoManager.printTasks();
        System.out.println("Enter task number to edit details: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) ;
            Task task = toDoManager.getTask(index);
            if (task == null) {
                System.out.println("Invalid task number.");
                return;
            }
            System.out.print("\n Editing: " + task.getName());
            System.out.println(" Press Enter to keep current value");

            System.out.print("New name [" + task.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                task.setName(name);
            }

            System.out.print("New description [" + task.getDescription() + "]: ");
            String desc = scanner.nextLine().trim();
            if (!desc.isEmpty()) {
                task.setDescription(desc);
            }

            System.out.print("Update priority? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                task.setPriority(selectPriority());
            }

            System.out.print("Update status? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                task.setStatus(selectStatus());
            }

            System.out.print("Update due date? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                LocalDate dueDate = inputDate();
                if (dueDate != null) {
                    task.setDueDate(dueDate);
                }
            }
            System.out.println("Task updated successfully");
            System.out.println(task);
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number.");
        }
    }

    private void deleteTask() {
        if (toDoManager.isEmpty()) {
            System.out.println("No tasks available");
        }
        toDoManager.printTasks();
        System.out.print("\nEnter task number to delete details: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) ;
            Task task = toDoManager.getTask(index);

            if (task == null) {
                System.out.println("Invalid task number.");
                return;
            }
            System.out.print("Are you sure you want to delete " + task.getName() + "? (y/n):");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                if (toDoManager.removeById(index)) {
                    System.out.println("Task deleted successfully");
                } else {
                    System.out.println("Deletion failed.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid task number.");
        }
    }

    private void markComplete() {
        changeTaskStatus(Task.TaskStatus.Completed);
    }

    private void markInProgress() {
        changeTaskStatus(Task.TaskStatus.InProgress);
    }

    private void changeTaskStatus(Task.TaskStatus newStatus) {
        if (toDoManager.isEmpty()) {
            System.out.println("No tasks available");
            return;
        }
        toDoManager.printTasks();
        System.out.print("\n Enter task number: ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            Task task = toDoManager.getTask(index);

            if (task == null) {
                System.out.println("Invalid task number.");
                return;
            }
            task.setStatus(newStatus);
            System.out.println("Task marked as " + newStatus + "!");
            System.out.println(task);
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number.");
        }
    }

    // Filtering and searching methods
    private void filterByStatus() {
        System.out.println("1. In Progress");
        System.out.println("2. Completed");
        System.out.print("Choose a status(number): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            Task.TaskStatus status = null;

            switch (choice) {
                case 1:
                    status = Task.TaskStatus.InProgress;
                    break;
                case 2:
                    status = Task.TaskStatus.Completed;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            toDoManager.printTasksByStatus(status);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void filterByPriority() {
        System.out.println("1. Urgent");
        System.out.println("2. High");
        System.out.println("3. Medium");
        System.out.println("4. Low");
        System.out.print("Choose priority(number): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            Task.Priority priority = null;

            switch (choice) {
                case 1:
                    priority = Task.Priority.URGENT;
                    break;
                case 2:
                    priority = Task.Priority.HIGH;
                    break;
                case 3:
                    priority = Task.Priority.MEDIUM;
                    break;
                case 4:
                    priority = Task.Priority.LOW;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            toDoManager.printTasksByPriority(priority);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void viewOverdue() {
        toDoManager.printOverdueTasks();
    }

    private void searchTasks() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        var results = toDoManager.searchTasks(keyword);

        if (results.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println(results.get(i));
            }
        }
    }

    //Sorting methods
    private void sortTasks() {
        System.out.println("====Sort tasks====");
        System.out.println("1. By Priority");
        System.out.println("2. By Status");
        System.out.println("3. By Name");
        System.out.println("4. By Due Date");
        System.out.println("5. By Created Date");
        System.out.println("Choose sorting option (number): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1:
                    toDoManager.sortByPriority();
                    System.out.println("Tasks sorted by priority.");
                    break;
                case 2:
                    toDoManager.sortByStatus();
                    System.out.println("Tasks sorted by status");
                    break;
                case 3:
                    toDoManager.sortByName();
                    System.out.println("Tasks sorted by name");
                    break;
                case 4:
                    toDoManager.sortByDueDate();
                    System.out.println("Tasks sorted by due date");
                    break;
                case 5:
                    toDoManager.sortByCreatedDate();
                    System.out.println("Tasks sorted by created date");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

        }
        catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
// Information Display Methods
    private void viewStatistics() {
        toDoManager.printStatistics();
    }
    private void viewDashBoard() {
        toDoManager.printDashBoard();
    }
//Bulk Operations
    private void clearCompleted(){
        if (toDoManager.isEmpty()) {
            System.out.println("No tasks available");
            return;
        }
        System.out.print(" Are you sure you want to delete all completed tasks? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            toDoManager.clearCompletedTasks();
        }
        else{
            System.out.println("Operation cancelled. ");
        }
    }
    private void clearAll(){
        if (toDoManager.isEmpty()) {
            System.out.println("No tasks available");
            return;
        }
        System.out.println(" Are you sure you want to delete all tasks? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Type 'DELETE ALL' to confirm");
            if(scanner.nextLine().trim().equals("DELETE ALL")){
                toDoManager.clearAll();
            }
            else{
                System.out.println("Operation cancelled.");
            }
        } else{
            System.out.println("Operation cancelled.");
        }
    }
  // Persistence Methods
    private void saveTasks() {
        System.out.print(" Enter filename(press Enter for default 'tasks.dat'): ");
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty()) {
            toDoManager.saveToFile();
        }
        else
        {
            toDoManager.saveToFile(filename);
        }
    }
    private void loadTasks() {
        System.out.println(" Enter filename(press Enter for default 'tasks.dat'");
        String filename = scanner.nextLine().trim();

        System.out.print(" This will replace current takss. Continue? (y/n): ");
        if(!scanner.nextLine().trim().equalsIgnoreCase("y")){
            System.out.println("Operation cancelled.");
            return;
        }
        if (filename.isEmpty()) {
            toDoManager.loadFromFile();
        }
        else{
            toDoManager.loadFromFile(filename);
        }
    }
    private void exit(){
        System.out.print("Save tasks before exiting? (y/n): ");
        if(scanner.nextLine().trim().equalsIgnoreCase("y")){
            toDoManager.saveToFile();
        }
        running = false;
    }
    // Helper Methods
    private Task.Priority selectPriority() {
        System.out.println("Select priority: ");
        System.out.println("1. URGENT");
        System.out.println("2. HIGH");
        System.out.println("3. MEDIUM");
        System.out.println("4. LOW");
        System.out.print("Choice (default: Medium): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1: return Task.Priority.URGENT;
                case 2: return Task.Priority.HIGH;
                case 3: return Task.Priority.MEDIUM;
                case 4: return Task.Priority.LOW;
                default: return Task.Priority.MEDIUM;

            }
        }catch (NumberFormatException e) {
            return Task.Priority.MEDIUM;
        }
    }

    private Task.TaskStatus selectStatus() {
        System.out.println("Select status:");
        System.out.println("1. In Progress");
        System.out.println("2. Completed");
        System.out.print("Choice (default: In Progress): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1: return Task.TaskStatus.InProgress;
                case 2: return Task.TaskStatus.Completed;
                default: return Task.TaskStatus.InProgress;
            }
        } catch (NumberFormatException e) {
            return Task.TaskStatus.InProgress;
        }
    }

    private LocalDate inputDate() {
        System.out.println("Enter date (DD-MM-YYYY) or 'today': ");

        String input = scanner.nextLine().trim();
        if(input.equalsIgnoreCase("today")){
            return LocalDate.now();
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(input, formatter);
        }
        catch (DateTimeParseException e) {
            System.out.println("Invalid date format.Skipping due date. ");
            return null;
        }
    }
    //Main method
    public static void main (String[] args) {
        ToDoApp app = new  ToDoApp();
        app.start();
    }

}
