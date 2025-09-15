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

import bong.ui.Ui;
import bong.task.Deadline;
import bong.task.Event;
import bong.task.Task;
import bong.task.Todo;

public class Storage {
    private final Path filePath;

    private static final DateTimeFormatter STORAGE_DATE_TIME_FORMAT =  
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"); 
    
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Storage(String filePath) {
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
            String[] parts = line.split("\\s*\\|\\s*", -1);
            // Expect at least: type | done | description
            if (parts.length < 3) {
                ui.showStorageCorruptionWarning(line);
                continue;
            }
            String type = parts[0].trim();
            boolean done = parts[1].trim().equals("1") || parts[1].trim().equalsIgnoreCase("true");

            try {

                switch (type) {
                    case "T":
                        Task todo = new Todo(parts[2].trim());
                        if (done) {
                            todo.setMark();
                        }
                        tasks.add(todo);
                        break;
                    case "D":
                        if (parts.length < 4) {
                            throw new IllegalArgumentException("missing deadline field");
                        }
                        LocalDateTime deadline = LocalDateTime.parse(parts[3].trim(), STORAGE_DATE_TIME_FORMAT);
                        Deadline deadlineTask = new Deadline(parts[2].trim(), deadline.format(INPUT_DATE_TIME_FORMAT));
                        if (done) {
                            deadlineTask.setUnmark();
                        }
                        tasks.add(deadlineTask);
                        break;
                    case "E":
                        if (parts.length < 5) {
                            throw new IllegalArgumentException("missing event fields");
                        }
                        LocalDateTime start = LocalDateTime.parse(parts[3].trim(), STORAGE_DATE_TIME_FORMAT);
                        LocalDateTime end = LocalDateTime.parse(parts[4].trim(), STORAGE_DATE_TIME_FORMAT);
                        Event eventTask = new Event(parts[2].trim(), start.format(INPUT_DATE_TIME_FORMAT), end.format(INPUT_DATE_TIME_FORMAT));
                        if (done) {
                            eventTask.setMark();
                        }
                        tasks.add(eventTask);
                        break;
                    default:
                        ui.showStorageCorruptionWarning(line);
                }
            } catch (Exception e ) {
                ui.showStorageCorruptionWarning(line +  " (" + e.getMessage() + ")");
            }
        }
        return tasks;
    }

    /* 
     * Saves all tasks to the storage file (overwrites).
     * 
     * @param tasks The list of tasks to save.
     * @throws IOException If writing fails
     */
    public void saveTasks(List<Task> tasks) throws IOException {
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
            return "D" + " | " + doneFlag + " | " + d.getDescription() + " | " + d.getDeadline().format(STORAGE_DATE_TIME_FORMAT);
        } else if (task instanceof Event) {
            Event e = (Event) task;
            return "E" + " | " + doneFlag + " | " + e.getDescription() + " | " + 
                e.getStart().format(STORAGE_DATE_TIME_FORMAT) + " | " + e.getEnd().format(STORAGE_DATE_TIME_FORMAT);
        }
        return "T" + " | " + doneFlag + " | " + task.getDescription();
    }
}
