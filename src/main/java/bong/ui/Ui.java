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
     * Displays a general response message to the user, typically the result of a command execution.
     *
     * @param message The message to be shown.
     */
    public void showResponse(String message) {
        System.out.println(message);
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
