package bong.ui;

import java.util.List;
import java.util.Scanner;

import bong.task.Task;

public class Ui {
    private Scanner scanner;
    private static final String LINE = "------------------------------";

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showWelcome() {
        System.out.println("    Hello! I'm Bong!\n    What can I do for you?");
        showLine();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLoadingError(String message) {
        System.out.println("    Failed to load tasks: " + message);
        showLine();
    }

    public void showExitMessage() {
        System.out.println("    Bye. Hope to see you again soon!");
    }

    public void showAddedTask(Task task, int totalTasks) {
        System.out.println("    Got it. I've added this task:");
        System.out.println("        " + task.toString());
        System.out.println("    " + "Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    public void showMarkedTask(Task task) {
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("        " + task.toString());
        showLine();
    }

    public void showUnmarkedTask(Task task) {
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("        " + task.toString());
        showLine();
    }

    public void showRemovedTask(Task task, int totalTasks) {
        System.out.println("    Noted. I've removed this task:");
        System.out.println("        " + task.toString());
        showLine();
    }

    public void showTaskList(List<Task> tasks) {
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + tasks.get(i).toString());
        }
        showLine();
    }

    public void showError(String message) {
        System.out.println(message);
        showLine();
    }

    public void showStorageCorruptionWarning(String line) {
        System.out.println("    Warning: Skipping corrupted line in storage: " + line);
        showLine();
    }

    public void closeScanner() {
        scanner.close();
    }
}
