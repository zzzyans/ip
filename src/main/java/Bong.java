import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "bong.txt";
    private static final Path STORAGE_PATH = Paths.get(DATA_DIR, DATA_FILE);

    private static final String LINE = "------------------------------";

    private static final String DEADLINE_DELIM = " /by ";

    private static final DateTimeFormatter STORAGE_DATE_TIME_FORMAT =  
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"); 

    /*
     * Command represents the supported user command types.
     */
    public static enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN
    }

    /*
     * Main method to run the Bong application.
     * It initialises the task list, loads existing tasks, and processes user commands.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        Ui ui = new Ui();
        ui.showWelcome();

        String userInput;

        List<Task> tasks = new ArrayList<>();

        try {
            loadTasks(tasks);
        } catch (IOException e) {
            ui.showLoadingError(e.getMessage());
        }

        while (true) {
            userInput = ui.readCommand();

            if (userInput.equals("bye")) {
                ui.showExitMessage();
                break;
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
                        saveTasks(tasks);
                        break;
                    case UNMARK:
                        String unmarkNumberString = userInput.substring(7);
                        int unmarkNumber = Integer.parseInt(unmarkNumberString);
                        if (unmarkNumber > tasks.size() || unmarkNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        tasks.get(unmarkNumber - 1).unmark();
                        ui.showUnmarkedTask(tasks.get(unmarkNumber - 1));
                        saveTasks(tasks);
                        break;
                    case TODO:
                        try {
                            String todoDescription = userInput.substring(5).trim();
                            if (todoDescription.isEmpty()) {
                                throw new BongException("A todo needs a description!");
                            }
                            tasks.add(new Todo(todoDescription));
                            ui.showAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                            saveTasks(tasks);
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
                            saveTasks(tasks);
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
                            saveTasks(tasks);
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
                        saveTasks(tasks);
                        break;
                    case UNKNOWN:
                    default:
                        throw new BongException("    Hmm, I dont understand that command.\n    Please try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete' or 'bye'.");
                }
            } catch (NumberFormatException e) {
                ui.showError("    The task number provided is invalid. Please enter a valid number.");
            } catch (BongException e) { 
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("An unexpected error occurred: " + e.getMessage());
            }
        }

        ui.closeScanner();
    }

    /*
     * Loads tasks from the from the storage file into the provided task list.
     * If the data directory or file does not exist, it will be created.
     * Corrupted lines in the storage file will be skipped with a warning.
     * 
     * @param tasks The list where loaded tasks will be added.
     * @throws IOException If an I/O error occurs while accessing the storage file.
     */
    private static void loadTasks(List<Task> tasks) throws IOException {
        Path parent = STORAGE_PATH.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(STORAGE_PATH)) {
            Files.createFile(STORAGE_PATH);
            return;
        }

        List<String> lines = Files.readAllLines(STORAGE_PATH, StandardCharsets.UTF_8);
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s*\\|\\s*", -1);
            // Expect at least: type | done | description
            if (parts.length < 3) {
                System.out.println("    Warning: Skipping corrupted line in storage: " + line);
                System.out.println(LINE);
                continue;
            }
            String type = parts[0].trim();
            boolean done = parts[1].trim().equals("1") || parts[1].trim().equalsIgnoreCase("true");

            try {

                DateTimeFormatter inpuDateTimeFormatForTaskConstructors = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

                switch (type) {
                    case "T":
                        Task todo = new Todo(parts[2].trim());
                        if (done) {
                            todo.mark();
                        }
                        tasks.add(todo);
                        break;
                    case "D":
                        if (parts.length < 4) {
                            throw new IllegalArgumentException("missing deadline field");
                        }
                        LocalDateTime deadline = LocalDateTime.parse(parts[3].trim(), STORAGE_DATE_TIME_FORMAT);
                        Deadline deadlineTask = new Deadline(parts[2].trim(), deadline.format(inpuDateTimeFormatForTaskConstructors));
                        if (done) {
                            deadlineTask.mark();
                        }
                        tasks.add(deadlineTask);
                        break;
                    case "E":
                        if (parts.length < 5) {
                            throw new IllegalArgumentException("missing event fields");
                        }
                        LocalDateTime start = LocalDateTime.parse(parts[3].trim(), STORAGE_DATE_TIME_FORMAT);
                        LocalDateTime end = LocalDateTime.parse(parts[4].trim(), STORAGE_DATE_TIME_FORMAT);
                        Event eventTask = new Event(parts[2].trim(), start.format(inpuDateTimeFormatForTaskConstructors), end.format(inpuDateTimeFormatForTaskConstructors));
                        if (done) {
                            eventTask.mark();
                        }
                        tasks.add(eventTask);
                        break;
                    default:
                        System.out.println("    Warning: Skipping unknown task type in storage: " + line);
                        System.out.println(LINE);
                }
            } catch (Exception e ) {
                System.out.println("    Warning: Skipping corrupted line in storage: " + line);
                System.out.println(LINE);
            }
        }
    }

    /* 
     * Saves all tasks to the storage file (overwrites).
     * 
     * @param tasks The list of tasks to save.
     * @throws IOException If writing fails
     */
    private static void saveTasks(List<Task> tasks) throws IOException {
        Path parent = STORAGE_PATH.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
            STORAGE_PATH, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (Task t : tasks) {
                    writer.write(serialiseTask(t));
                    writer.newLine();
                }
        }
    }

    /*
     * Serialise a task into a single-line storage representation.
     * 
     * @param task The task to serialise.
     * @return Serialised string.
     */
    private static String serialiseTask(Task task) {
        String doneFlag = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T" + " | " + doneFlag + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D" + " | " + doneFlag + " | " + d.getDescription() + " | " + d.getDeadline().format(STORAGE_DATE_TIME_FORMAT);
        } else if (task instanceof Event) {
            Event e = (Event) task;
            return "E" + " | " + doneFlag + " | " + e.getDescription() + " | " + 
                e.getStart().format(STORAGE_DATE_TIME_FORMAT) + " | " + e.getEnd().format(STORAGE_DATE_TIME_FORMAT);
        }
        return "T" + " | " + doneFlag + " | " + task.getDescription();
    }
}
