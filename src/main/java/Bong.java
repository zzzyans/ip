import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Bong is a simple task management command-line application.
 * It allows users to add, list, mark, unmark, and delete tasks.
 * Tasks can be Todos, Deadlines, or Events.
 */
public class Bong {

    /* Storage configuration */
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "bong.txt";
    private static final Path STORAGE_PATH = Paths.get(DATA_DIR, DATA_FILE);

    private static final String LINE = "------------------------------";

    /*
     * Command represents the supported user command types.
     */
    public static enum Command {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN
    }

    public static class Task {
        protected String description;
        protected boolean isDone;
    
        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }
    
        public String getStatusIcon() {
            return (this.isDone ? "X" : " "); 
        }

        public String getDescription() {
            return this.description;
        }

        public boolean isDone() {
            return this.isDone;
        }

        public void mark() {
            this.isDone = true;
        }

        public void unmark() {
            this.isDone = false;
        }

        @Override
        public String toString() {
            return "[" + this.getStatusIcon() + "] " + this.description;
        } 
    }

    public static class Todo extends Task {
        public Todo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }

    }

    public static class Deadline extends Task {
        protected String deadline;

        public Deadline(String description, String deadline) {
            super(description);
            this.deadline = deadline;
        }

        public String getDeadline() {
            return this.deadline;
        }

        @Override
        public String toString() {
            return "[D]" + super.toString() + " (by: " + this.deadline + ")";
        }

    }

    public static class Event extends Task {
        protected String start;
        protected String end;

        public Event(String description, String start, String end) {
            super(description);
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return this.start;
        }

        public String getEnd() {
            return this.end;
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
        }
    }

    public static class BongException extends Exception {
        public BongException(String msg) {
            super(msg);
        }
    }

    /*
     * Main method to run the Bong application.
     * It initialises the task list, loads existing tasks, and processes user commands.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        System.out.println("    Hello! I'm Bong!\n    What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        String userInput;

        List<Task> tasks = new ArrayList<>();

        try {
            loadTasks(tasks);
        } catch (IOException e) {
            System.out.println("    Failed to load tasks: " + e.getMessage());
            System.out.println(LINE);
        }

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                System.out.println("    Bye. Hope to see you again soon!");
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
                        int listNumber = 1;
                        System.out.println("    Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println("    " + listNumber + ". " + tasks.get(i).toString());
                            listNumber++;
                        }
                        System.out.println(LINE);
                        break;
                    case MARK:
                        String markNumberString = userInput.substring(5);
                        int markNumber = Integer.parseInt(markNumberString);
                        if (markNumber > tasks.size() || markNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        tasks.get(markNumber - 1).mark();
                        System.out.println("    Nice! I've marked this task as done:");
                        System.out.println("        " + tasks.get(markNumber - 1).toString());
                        System.out.println(LINE);
                        saveTasks(tasks);
                        break;
                    case UNMARK:
                        String unmarkNumberString = userInput.substring(7);
                        int unmarkNumber = Integer.parseInt(unmarkNumberString);
                        if (unmarkNumber > tasks.size() || unmarkNumber <= 0) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        tasks.get(unmarkNumber - 1).unmark();
                        System.out.println("    OK, I've marked this task as not done yet:");
                        System.out.println("        " + tasks.get(unmarkNumber - 1).toString());
                        System.out.println(LINE);
                        saveTasks(tasks);
                        break;
                    case TODO:
                        try {
                            String todoDescription = userInput.substring(5).trim();
                            if (todoDescription.isEmpty()) {
                                throw new BongException("A todo needs a description!");
                            }
                            tasks.add(new Todo(todoDescription));
                            System.out.println("    Got it. I've added this task:");
                            System.out.println("        " + tasks.get(tasks.size() - 1).toString());
                            System.out.println("    " + "Now you have " + tasks.size() + " tasks in the list.");
                            System.out.println(LINE);
                            saveTasks(tasks);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("A todo needs a description!");
                        }
                    case DEADLINE:
                        try {
                            String[] deadlineParts = userInput.split(" /by ", 2);
                            if (deadlineParts.length < 2) {
                                throw new BongException("Looks like your 'deadline' is missing details! Format: deadline <description> /by time");
                            }
                            String deadlineDescription = deadlineParts[0].substring(9).trim();
                            String deadlineTime = deadlineParts[1].trim();
                            if (deadlineDescription.isEmpty() || deadlineTime.isEmpty()) {
                                throw new BongException("Looks like your 'deadline' is missing details! Description or deadline cannot be empty.");
                            }
                            tasks.add(new Deadline(deadlineDescription, deadlineTime));
                            System.out.println("    Got it. I've added this task:");
                            System.out.println("        " + tasks.get(tasks.size() - 1).toString());
                            System.out.println("    " + "Now you have " + tasks.size() + " tasks in the list.");
                            System.out.println(LINE);
                            saveTasks(tasks);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("Looks like your 'deadline' is missing details! Format: deadline <description> /by time");
                        }
                    case EVENT:
                        try {
                            String regex = " /from | /to ";
                            String[] eventParts = userInput.split(regex, 3);
                            if (eventParts.length < 3) {
                                throw new BongException("Looks like your 'event' is missing details! Format: event <description> /from <start time> /to <end time>");
                            }
                            String description = eventParts[0].substring(6).trim();
                            String startTime = eventParts[1].trim();
                            String endTime = eventParts[2].trim();
                            if (description.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                                throw new BongException("Looks like your 'event' is missing details! Description, start time and end time cannot be empty.");
                            }
                            tasks.add(new Event(description, eventParts[1], eventParts[2]));
                            System.out.println("    Got it. I've added this task:");
                            System.out.println("        " + tasks.get(tasks.size() - 1).toString());
                            System.out.println("    " + "Now you have " + tasks.size() + " tasks in the list.");
                            System.out.println(LINE);
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
                        System.out.println("    Noted. I've removed this task:");
                        System.out.println("        " + removedTask);
                        System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println(LINE);
                        saveTasks(tasks);
                        break;
                    case UNKNOWN:
                    default:
                        throw new BongException("    Hmm, I dont understand that command.\n    Please try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete' or 'bye'.");
                }
            } catch (NumberFormatException e) {
                System.out.println("    The task number provided is invalid. Please enter a valid number.");
                System.out.println(LINE);
            } catch (BongException e) { 
                System.out.println(e.getMessage());
                System.out.println(LINE);
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println(LINE);
            }
        }

        scanner.close();
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
                continue;
            }
            String type = parts[0].trim();
            boolean done = parts[1].trim().equals("1") || parts[1].trim().equalsIgnoreCase("true");

            try {
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
                        Task deadline = new Deadline(parts[2].trim(), parts[3].trim());
                        if (done) {
                            deadline.mark();
                        }
                        tasks.add(deadline);
                        break;
                    case "E":
                        if (parts.length < 5) {
                            throw new IllegalArgumentException("missing event fields");
                        }
                        Task event = new Event(parts[2].trim(), parts[3].trim(), parts[4].trim());
                        if (done) {
                            event.mark();
                        }
                        tasks.add(event);
                        break;
                    default:
                        System.out.println("    Warning: Skipping unknown task type in storage: " + line);
                }
            } catch (Exception e ) {
                System.out.println("    Warning: Skipping corrupted line in storage: " + line);
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
            return "D" + " | " + doneFlag + " | " + d.getDescription() + " | " + d.getDeadline();
        } else if (task instanceof Event) {
            Event e = (Event) task;
            return "E" + " | " + doneFlag + " | " + e.getDescription() + " | " + e.getStart() + " to " + e.getEnd();
        }
        return "T" + " | " + doneFlag + " | " + task.getDescription();
    }
}
