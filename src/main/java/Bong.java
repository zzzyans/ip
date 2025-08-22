import java.util.Scanner;
import java.util.ArrayList;

public class Bong {

    public static class Task {
        protected String description;
        protected boolean isDone;
    
        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }
    
        public String getStatusIcon() {
            return (this.isDone ? "X" : " "); // mark done task with X
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

    public static void main(String[] args) {
        String line = "------------------------------";

        System.out.println("    Hello! I'm Bong!\n    What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        String userInput;

        ArrayList<Task> list = new ArrayList<>();
        int count = 0;

        while (true) {
            userInput = scanner.nextLine();

            try {
                if (userInput.equals("bye")) {
                    break;
                } else if (userInput.equals("list")) {
                    int listNumber = 1;
                    System.out.println("    Here are the tasks in your list:");
                    for (int i = 0; i < count; i++) {
                        System.out.println("    " + listNumber + ". " + list.get(i).toString());
                        listNumber++;
                    }
                    System.out.println(line);
                } else if (userInput.startsWith("mark ")) {
                    String taskNumberString = userInput.substring(5);
                    int taskNumber = Integer.parseInt(taskNumberString);
                    if (taskNumber > count) {
                        throw new BongException("You do not have this many tasks in your list!");
                    }
                    list.get(taskNumber - 1).mark();
                    System.out.println("    Nice! I've marked this task as done:");
                    System.out.println("        " + list.get(taskNumber - 1).toString());
                    System.out.println(line);
                } else if (userInput.startsWith("unmark ")) {
                    String taskNumberString = userInput.substring(7);
                    int taskNumber = Integer.parseInt(taskNumberString);
                    if (taskNumber > count) {
                        throw new BongException("You do not have this many tasks in your list!");
                    }
                    list.get(taskNumber - 1).unmark();
                    System.out.println("    OK, I've marked this task as not done yet:");
                    System.out.println("        " + list.get(taskNumber - 1).toString());
                    System.out.println(line);
                } else if (userInput.startsWith("todo ")) {
                    String description = userInput.substring(5);
                    if (description.isEmpty()) {
                        throw new BongException("A todo needs a description!");
                    }
                    list.add(new Todo(description));
                    System.out.println("    Got it. I've added this task:");
                    System.out.println("        " + list.get(count).toString());
                    count++;
                    System.out.println("    " + "Now you have " + count + " tasks in the list.");
                    System.out.println(line);
                } else if (userInput.startsWith("deadline ")) {
                    String[] parts = userInput.split(" /by ");
                    String description = parts[0].substring(9);
                    if (description.isBlank() || parts[1].isBlank()) {
                        throw new BongException("Looks like your 'deadline' is missing details! Description or deadline cannot be empty.");
                    }
                    list.add(new Deadline(description, parts[1]));
                    System.out.println("    Got it. I've added this task:");
                    System.out.println("        " + list.get(count).toString());
                    count++;
                    System.out.println("    " + "Now you have " + count + " tasks in the list.");
                    System.out.println(line);
                } else if (userInput.startsWith("event ")) {
                    String regex = " /from | /to ";
                    String[] parts = userInput.split(regex);
                    String description = parts[0].substring(6);
                    if (description.isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
                        throw new BongException("Looks like your 'event' is missing details! Description, start time and end time cannot be empty.");
                    }
                    list.add(new Event(description, parts[1], parts[2]));
                    System.out.println("    Got it. I've added this task:");
                    System.out.println("        " + list.get(count).toString());
                    count++;
                    System.out.println("    " + "Now you have " + count + " tasks in the list.");
                    System.out.println(line);
                } else if (userInput.startsWith("delete ")) {
                    String taskNumberString = userInput.substring(7);
                    int taskNumber = Integer.parseInt(taskNumberString);
                    if (taskNumber > count) {
                        throw new BongException("You do not have this many tasks in your list!");
                    }
                    Task removedTask = list.remove(taskNumber - 1);
                    count--;
                    System.out.println("    Noted. I've removed this task:");
                    System.out.println("        " + removedTask);
                    System.out.println("    Now you have " + count + " tasks in the list.");
                    System.out.println(line);
                } else {
                    throw new BongException("Hmm, I don't understand that command.");
                }
            } catch (BongException e) { 
                System.out.println(e.getMessage());
                System.out.println(line);
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println(line);
            }
        }

        System.out.println("    Bye. Hope to see you again soon!");

        scanner.close();
    }
}
