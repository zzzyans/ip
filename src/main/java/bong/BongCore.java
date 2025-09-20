package bong;

import bong.command.Command;
import bong.exception.BongException;
import bong.parser.Parser;
import bong.storage.Storage;
import bong.task.TaskList;
import bong.ui.Ui;

import java.io.IOException;

/**
 * Encapsulates the core logic of the Bong task management application.
 * This class handles initialisation of Ui, Storage and TaskList, and provides a
 * method to get responses from commands, suitable for both console and GUI interfaces.
 */
public class BongCore {
    // File path where tasks are persistently stored
    private static final String FILE_PATH = "data/bong.txt";

    /**
     * Represents the supported user command types in the Bong application.
     */
    public static enum CommandType {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, BYE, UNKNOWN
    }

    // Instance variables for the core components
    private Ui ui;
    private Storage storage;
    private TaskList tasks;

    /**
     * Constructs a BongCore instance, initialising its components and loading tasks.
     * This constructor sets up the essential backend components for the Bong application.
     */
    public BongCore() {
        ui = new Ui();
        storage = new Storage(FILE_PATH);
        try {
            tasks = new TaskList(storage.loadTasks(ui));
        } catch (IOException e) {
            ui.showLoadingError("Failed to load tasks: " + e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Processes a user input command and returns a response string.
     * Useful for GUI where direct console interaction by UI/Command classes is undesirable.
     *
     * @param input Raw command string from the user.
     * @return String containing the response from the executed command, or an error message.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            return c.execute(tasks, ui, storage);
        } catch (BongException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    /**
     * Returns the welcome message for initial text on GUI.
     *
     * @return The welcome string.
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Bong!\nWhat can I do for you?";
    }

    /**
     * Runs the console version of the Bong application.
     * This uses the shared core logic (getResponse) but handles console-specific input/output.
     */
    public void runConsole() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = ui.readCommand();
            String response = getResponse(fullCommand);

            try {
                Command command = Parser.parse(fullCommand);
                isExit = command.isExit();
            } catch (BongException ignored) {

            }

            ui.showResponse(response);
        }
        ui.closeScanner();
    }
}
