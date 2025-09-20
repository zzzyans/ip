package bong.storage;
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

import bong.exception.BongException;
import bong.ui.Ui;
import bong.task.Deadline;
import bong.task.Event;
import bong.task.Task;
import bong.task.Todo;
import bong.util.DateTimeUtil;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 * File format (single-line per task):
 *  T | done | description
 *  D | done | description | yyyy-MM-dd HHmm
 *  E | done | description | yyyy-MM-dd HHmm | yyyy-MM-dd HHmm
 */
public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        assert filePath != null : "filePath must not be null";
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the storage file into a list of Task objects.
     * If the data directory or file does not exist, it will be created.
     * Corrupted lines in the storage file will be skipped with a warning displayed via the UI.
     * 
     * @param ui The Ui object for displaying warnings about corrupted data.
     * @return A List<Task> containing the tasks loaded from the file.
     * @throws IOException If an I/O error occurs while accessing the storage file.
     */
    public List<Task> loadTasks(Ui ui) throws IOException {
        assert ui != null : "Ui must not be null when loading tasks";
        List<Task> tasks = new ArrayList<>();
        Path parent = filePath.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            try {
                Task t = parseLineToTask(line);
                tasks.add(t);
            } catch (Exception e) {
                ui.showStorageCorruptionWarning(line + " (" + e.getMessage() + ")");
            }
        }
        return tasks;
    }

    private Task parseLineToTask(String line) throws BongException {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        // Expect at least: type | done | description
        if (parts.length < 3) {
            throw new IllegalArgumentException("invalid storage line: expected at least 3 parts");
        }
        String type = parts[0].trim();
        boolean done = parts[1].trim().equals("1") || parts[1].trim().equalsIgnoreCase("true");

        switch (type) {
            case "T":
                return createTodo(parts, done);
            case "D":
                return createDeadline(parts, done);
            case "E":
                return createEvent(parts, done);
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }

    private Task createTodo(String[] parts, boolean done) {
        String description = parts[2].trim();
        Task todo = new Todo(description);
        if (done) {
            todo.setMark();
        }
        return todo;
    }

    private Task createDeadline(String[] parts, boolean done) throws BongException {
        if (parts.length < 4) {
            throw new IllegalArgumentException("missing deadline field");
        }
        LocalDateTime deadline = LocalDateTime.parse(parts[3].trim(), DateTimeUtil.STORAGE);
        Deadline deadlineTask = new Deadline(parts[2].trim(), deadline.format(DateTimeUtil.INPUT));
        if (done) {
            deadlineTask.setMark();
        }
        return deadlineTask;
    }

    private Task createEvent(String[] parts, boolean done) throws BongException {
        if (parts.length < 5) {
            throw new IllegalArgumentException("missing event fields");
        }
        LocalDateTime start = LocalDateTime.parse(parts[3].trim(), DateTimeUtil.STORAGE);
        LocalDateTime end = LocalDateTime.parse(parts[4].trim(), DateTimeUtil.STORAGE);
        Event eventTask = new Event(parts[2].trim(), start.format(DateTimeUtil.INPUT), end.format(DateTimeUtil.INPUT));
        if (done) {
            eventTask.setMark();
        }
        return eventTask;
    }

    /* 
     * Saves all tasks to the storage file (overwrites).
     * 
     * @param tasks The list of tasks to save.
     * @throws IOException If writing fails
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        assert tasks != null : "saveTasks requires a non-null list";
        Path parent = filePath.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
            filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
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
    private String serialiseTask(Task task) {
        String doneFlag = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T" + " | " + doneFlag + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D" + " | " + doneFlag + " | " + d.getDescription() + " | " + d.getDeadline().format(DateTimeUtil.STORAGE);
        } else if (task instanceof Event) {
            Event e = (Event) task;
            return "E" + " | " + doneFlag + " | " + e.getDescription() + " | " + 
                e.getStart().format(DateTimeUtil.STORAGE) + " | " + e.getEnd().format(DateTimeUtil.STORAGE);
        }
        return "T" + " | " + doneFlag + " | " + task.getDescription();
    }
}
