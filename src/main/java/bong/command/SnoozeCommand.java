package bong.command;

import bong.exception.BongException;
import bong.storage.Storage;
import bong.task.Deadline;
import bong.task.Event;
import bong.task.Task;
import bong.task.TaskList;
import bong.ui.Ui;
import bong.util.DateTimeUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a command to reschedule (snooze) a Deadline or Event task.
 * Inherits from the abstract Command class.
 */
public class SnoozeCommand extends Command {
    private final int taskNumber;
    private final String newStartString;
    private final String newEndString;

    /**
     * Constructs a SnoozeCommand.
     *
     * @param taskNumber 1-based index of task to snooze.
     * @param newStartString New start / deadline datetime string.
     * @param newEndString New end datetime string for events.
     */
    public SnoozeCommand(int taskNumber, String newStartString, String newEndString) {
        this.taskNumber = taskNumber;
        this.newStartString = newStartString;
        this.newEndString = newEndString;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws BongException {
        Task task = tasks.getTask(taskNumber);

        if (task instanceof Deadline) {
            snoozeDeadline((Deadline) task, tasks, storage);
            return "Snoozed deadline:\n" + task.toString();
        }

        if (task instanceof Event) {
            snoozeEvent((Event) task, tasks, storage);
            return "Snoozed event:\n" + task.toString();
        }

        throw new BongException("Cannot snooze task type - only deadlines and events can be snoozed.");
    }

    private void snoozeDeadline(Deadline deadline, TaskList tasks, Storage storage) throws BongException {
        LocalDateTime newDeadline = parseDate(newStartString, "new deadline");
        deadline.setDeadline(newDeadline);
        saveTasks(tasks, storage);
    }

    private void snoozeEvent(Event event, TaskList tasks, Storage storage) throws BongException {
        if (newEndString == null || newEndString.isBlank()) {
            throw new BongException("Snoozing an event requires both new start and end times.");
        }
        LocalDateTime newStart = parseDate(newStartString, "event start");
        LocalDateTime newEnd = parseDate(newEndString, "event end");
        event.setStart(newStart);
        event.setEnd(newEnd);
        saveTasks(tasks, storage);
    }

    private LocalDateTime parseDate(String value, String fieldDescription) throws BongException {
        if (value == null || value.isBlank()) {
            throw new BongException("Snooze requires a non-empty " + fieldDescription + " in 'yyyy-MM-dd HHmm' format.");
        }
        try {
            return LocalDateTime.parse(value, DateTimeUtil.INPUT);
        } catch (DateTimeParseException e) {
            throw new BongException("Invalid date/time format for " + fieldDescription + ". Use 'yyyy-MM-dd HHmm'.");
        }
    }

    private void saveTasks(TaskList tasks, Storage storage) throws BongException {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException e) {
            throw new BongException("Failed to save tasks after snooze: " + e.getMessage());
        }
    }
}