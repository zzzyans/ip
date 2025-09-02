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

    private static final String DEADLINE_DELIM = " /by ";

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
            String userInput = ui.readCommand();
            
            if (userInput.equals("bye")) {
                ui.showExitMessage();
                isExit = true;
                continue;
            }

            try {
                Command command = Command.UNKNOWN;

                String[] inputParts = userInput.split(" ", 2);
                String commandWord = inputParts[0].toUpperCase();
                if (commandWord.isEmpty()) {
                    command = Command.UNKNOWN;
                } else {
                    try {
                        command = Command.valueOf(commandWord);
                    } catch (IllegalArgumentException e) {

                    }
                } 

                switch (command) {
                    case LIST:
                        ui.showTaskList(tasks);
                        break;
                    case MARK:
                        String markNumberString = userInput.substring(5);
                        int markNumber = Integer.parseInt(markNumberString);
                        if (markNumber > tasks.size() || markNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        tasks.get(markNumber - 1).mark();
                        ui.showMarkedTask(tasks.get(markNumber - 1));
                        storage.saveTasks(tasks);
                        break;
                    case UNMARK:
                        String unmarkNumberString = userInput.substring(7);
                        int unmarkNumber = Integer.parseInt(unmarkNumberString);
                        if (unmarkNumber > tasks.size() || unmarkNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        tasks.get(unmarkNumber - 1).unmark();
                        ui.showUnmarkedTask(tasks.get(unmarkNumber - 1));
                        storage.saveTasks(tasks);
                        break;
                    case TODO:
                        try {
                            String todoDescription = userInput.substring(5).trim();
                            if (todoDescription.isEmpty()) {
                                throw new BongException("A todo needs a description!");
                            }
                            tasks.add(new Todo(todoDescription));
                            ui.showAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                            storage.saveTasks(tasks);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("A todo needs a description!");
                        }
                    case DEADLINE:
                        try {
                            String[] deadlineParts = userInput.split(DEADLINE_DELIM, 2);
                            if (deadlineParts.length < 2) {
                                throw new BongException("Looks like your 'deadline' is missing details! Try 'deadline <description> /by <yyyy-MM-dd HHmm>.'");
                            }
                            String deadlineDescription = deadlineParts[0].substring(9).trim();
                            String deadlineTime = deadlineParts[1].trim();
                            if (deadlineDescription.isEmpty() || deadlineTime.isEmpty()) {
                                throw new BongException("Looks like your 'deadline' is missing details! Description or deadline cannot be empty.");
                            }
                            tasks.add(new Deadline(deadlineDescription, deadlineTime));
                            ui.showAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                            storage.saveTasks(tasks);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("Looks like your 'deadline' is missing details! Try 'deadline <description> /by <yyyy-MM-dd HHmm>.'");
                        }
                    case EVENT:
                        try {
                            String regex = " /from | /to ";
                            String[] eventParts = userInput.split(regex, 3);
                            if (eventParts.length < 3) {
                                throw new BongException("Looks like your 'event' is missing details! Try 'event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>.'");
                            }
                            String description = eventParts[0].substring(6).trim();
                            String startTime = eventParts[1].trim();
                            String endTime = eventParts[2].trim();
                            if (description.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                                throw new BongException("Looks like your 'event' is missing details! Description, start time and end time cannot be empty.");
                            }
                            tasks.add(new Event(description, eventParts[1], eventParts[2]));
                            ui.showAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                            storage.saveTasks(tasks);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("Looks like your 'event' is missing details! Description, start time and end time cannot be empty.");
                        }
                    case DELETE:
                        String taskNumberString = userInput.substring(7);
                        int taskNumber = Integer.parseInt(taskNumberString);
                        if (taskNumber > tasks.size() || taskNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        Task removedTask = tasks.remove(taskNumber - 1);
                        ui.showRemovedTask(removedTask, tasks.size());
                        storage.saveTasks(tasks);
                        break;
                    case UNKNOWN:
                    default:
                        throw new BongException("    Hmm, I dont understand that command.\n    Please try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete' or 'bye'.");
                }
            } catch (NumberFormatException e) {
                ui.showError("    The task number provided is invalid. Please enter a valid number.");
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
