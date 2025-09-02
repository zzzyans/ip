import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Bong is a simple task management command-line application.
 * It allows users to add, list, mark, unmark, and delete tasks.
 * Tasks can be Todos, Deadlines, or Events.
 * Tasks are persisted to ./data/bong.txt.
 * Supports date and time for Deadlines and Events.
 */
public class Bong {

    /* Storage configuration */
    private static final String FILE_PATH = "data/bong.txt";

    /*
     * Command represents the supported user command types.
     */
    public static enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN
    }

    // Instance variables for the core components
    private Ui ui;
    private Storage storage;
    private List<Task> tasks;

    /*
     * Constructs a Bong application instance.
     * Initialises UI, Storage, and loads tasks.
     */
    public Bong(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = storage.loadTasks(ui);
        } catch (IOException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new ArrayList<>();
        }
    }

    public void run() {
        ui.showWelcome();

        boolean isExit = false;

        while (!isExit) {
            String fullCommand = ui.readCommand();
            
            if (fullCommand.equals("bye")) {
                ui.showExitMessage();
                isExit = true;
                continue;
            }

            try {
                Parser.ParsedCommand parsedCommand = Parser.parse(fullCommand);

                switch (parsedCommand.command) {
                    case LIST:
                        ui.showTaskList(tasks);
                        break;
                    case MARK:
                        int markNumber = parsedCommand.taskNumber;
                        if (markNumber > tasks.size() || markNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        tasks.get(markNumber - 1).mark();
                        ui.showMarkedTask(tasks.get(markNumber - 1));
                        storage.saveTasks(tasks);
                        break;

                    case UNMARK:
                        int unmarkNumber = parsedCommand.taskNumber;
                        if (unmarkNumber > tasks.size() || unmarkNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        tasks.get(unmarkNumber - 1).unmark();
                        ui.showUnmarkedTask(tasks.get(unmarkNumber - 1));
                        storage.saveTasks(tasks);
                        break;
                    case TODO:
                        tasks.add(new Todo(parsedCommand.description));
                        ui.showAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                        storage.saveTasks(tasks);
                        break;
                    case DEADLINE:
                        tasks.add(new Deadline(parsedCommand.description, parsedCommand.deadline));
                        ui.showAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                        storage.saveTasks(tasks);
                        break;
                    case EVENT:
                        tasks.add(new Event(parsedCommand.description, parsedCommand.eventStart, parsedCommand.eventEnd));
                        ui.showAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                        storage.saveTasks(tasks);
                        break;
                    case DELETE:
                        int taskNumber = parsedCommand.taskNumber;
                        if (taskNumber > tasks.size() || taskNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        Task removedTask = tasks.remove(taskNumber - 1);
                        ui.showRemovedTask(removedTask, tasks.size());
                        storage.saveTasks(tasks);
                        break;
                    case UNKNOWN:
                        throw new BongException("Command UNKNOWN was return by Parser without an exception.");
                }
            } catch (BongException e) { 
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("    An error occurred while saving tasks: " + e.getMessage());
            } catch (Exception e) {
                ui.showError("An unexpected error occurred: " + e.getMessage());
            }
        }

        ui.closeScanner();
    }

    /*
     * Main method to launch the Bong application.
     * 
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        new Bong(FILE_PATH).run();
    }
}
