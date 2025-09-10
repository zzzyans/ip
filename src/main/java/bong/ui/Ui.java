package bong.ui;

import java.util.List;
import java.util.Scanner;

import bong.task.Task;

/**
 * Deals with interaction with the user.
 * The Ui class is responsible for all input and output operations,
 * including displaying messages, reading commands, and showing errors or warnings.
 */
public class Ui {
    private Scanner scanner;
    private static final String LINE = "------------------------------";

    /**
     * Constructs a Ui object.
     * Initialises a new Scanner to read input from System.in.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays a horizontal separator line to format output.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays the welcome message when the Bong application starts.
     */
    public void showWelcome() {
        System.out.println("    Hello! I'm Bong!\n    What can I do for you?");
        showLine();
    }

    /**
     * Reads a single line of command input from the user.
     *
     * @return The trimmed string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays an error message indicating that tasks failed to load from storage.
     *
     * @param message The specific error message about the loading failure.
     */
    public void showLoadingError(String message) {
        System.out.println("    Failed to load tasks: " + message);
        showLine();
    }

    /**
     * Displays the exit message when the user quits the application.
     */
    public void showExitMessage() {
        System.out.println("    Bye. Hope to see you again soon!");
    }

    /**
     * Displays a confirmation message after a new task has been added.
     *
     * @param task The Task that was added.
     * @param totalTasks The total number of tasks currently in the list.
     */
    public void showAddedTask(Task task, int totalTasks) {
        System.out.println("    Got it. I've added this task:");
        System.out.println("        " + task.toString());
        System.out.println("    " + "Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a confirmation message after a task has been marked as done.
     *
     * @param task The Task that was marked.
     */
    public void showMarkedTask(Task task) {
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("        " + task.toString());
        showLine();
    }

    /**
     * Displays a confirmation message after a task has been marked as not done.
     *
     * @param task The Task that was unmarked.
     */
    public void showUnmarkedTask(Task task) {
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("        " + task.toString());
        showLine();
    }

    /**
     * Displays a confirmation message after a task has been removed from the list.
     *
     * @param task The Task that was removed.
     * @param totalTasks The total number of tasks remaining in the list.
     */
    public void showRemovedTask(Task task, int totalTasks) {
        System.out.println("    Noted. I've removed this task:");
        System.out.println("        " + task.toString());
        showLine();
    }

    /**
     * Displays the entire list of tasks to the user.
     * Each task is numbered starting from 1.
     *
     * @param tasks The List<Task> to be displayed.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + tasks.get(i).toString());
        }
        showLine();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be shown.
     */
    public void showError(String message) {
        System.out.println(message);
        showLine();
    }

    /**
     * Displays a warning message about a corrupted line found in the storage file.
     *
     * @param line The corrupted line from the storage file.
     */
    public void showStorageCorruptionWarning(String line) {
        System.out.println("    Warning: Skipping corrupted line in storage: " + line);
        showLine();
    }

    /**
     * Closes the scanner used for reading user input.
     */
    public void closeScanner() {
        scanner.close();
    }
}
