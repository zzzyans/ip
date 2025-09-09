package bong.task;

/**
 * Represents a generic task in the Bong application.
 * This is the base class for different types of tasks (Todo, Deadline, Event).
 * It stores a description and a completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task object with the given description.
     * A new task is always initialised as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns a string representing the status icon of the task.
     * "X" if the task is done, " " (space) if it's not done.
     *
     * @return The status icon string.
     */
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