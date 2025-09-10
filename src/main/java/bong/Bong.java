package bong;

import java.io.IOException;
import java.util.List;

import bong.ui.Ui;
import bong.storage.Storage;
import bong.parser.Parser;
import bong.task.Task;
import bong.task.Todo;
import bong.task.Deadline;
import bong.task.Event;
import bong.task.TaskList;
import bong.exception.BongException;

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
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, UNKNOWN
    }

    // Instance variables for the core components
    private Ui ui;
    private Storage storage;
    private TaskList tasks;

    /*
     * Constructs a Bong application instance.
     * Initialises UI, Storage, and loads tasks.
     */
    public Bong(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasks(ui));
        } catch (IOException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new TaskList();
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
                        ui.showTaskList(tasks.getTasks());
                        break;
                    case MARK:
                        Task markedTask = tasks.markTask(parsedCommand.taskNumber);
                        ui.showMarkedTask(markedTask);
                        storage.saveTasks(tasks.getTasks());
                        break;
                    case UNMARK:
                        Task unmarkedTask = tasks.unmarkTask(parsedCommand.taskNumber);
                        ui.showUnmarkedTask(unmarkedTask);
                        storage.saveTasks(tasks.getTasks());
                        break;
                    case TODO:
                        Task newTodo = new Todo(parsedCommand.description);
                        tasks.addTask(newTodo);
                        ui.showAddedTask(newTodo, tasks.size());
                        storage.saveTasks(tasks.getTasks());
                        break;
                    case DEADLINE:
                        Task newDeadline = new Deadline(parsedCommand.description, parsedCommand.deadline);
                        tasks.addTask(newDeadline);
                        ui.showAddedTask(newDeadline, tasks.size());
                        storage.saveTasks(tasks.getTasks());
                        break;
                    case EVENT:
                        Task newEvent = new Event(parsedCommand.description, parsedCommand.eventStart, parsedCommand.eventEnd);
                        tasks.addTask(newEvent);
                        ui.showAddedTask(newEvent, tasks.size());
                        storage.saveTasks(tasks.getTasks());
                        break;
                    case DELETE:
                        Task removedTask = tasks.deleteTask(parsedCommand.taskNumber);
                        ui.showRemovedTask(removedTask, tasks.size());
                        storage.saveTasks(tasks.getTasks());
                        break;
                    case FIND:
                        List<Task> matchingTasks = tasks.findTasks(parsedCommand.keyword);
                        ui.showMatchingTasks(matchingTasks);
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
