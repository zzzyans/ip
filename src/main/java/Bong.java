import java.util.Scanner;
import java.util.ArrayList;

public class Bong {

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

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                System.out.println("    Bye. Hope to see you again soon!");
                break;
            }

            try {
                Command command = Command.UNKNOWN;
                String commandWord = userInput.split(" ", 2)[0].toUpperCase();
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
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println("    " + listNumber + ". " + list.get(i).toString());
                            listNumber++;
                        }
                        System.out.println(line);
                        break;
                    case MARK:
                        String markNumberString = userInput.substring(5);
                        int markNumber = Integer.parseInt(markNumberString);
                        if (markNumber > list.size()) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        list.get(markNumber - 1).mark();
                        System.out.println("    Nice! I've marked this task as done:");
                        System.out.println("        " + list.get(markNumber - 1).toString());
                        System.out.println(line);
                        break;
                    case UNMARK:
                        String unmarkNumberString = userInput.substring(7);
                        int unmarkNumber = Integer.parseInt(unmarkNumberString);
                        if (unmarkNumber > list.size()) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        list.get(unmarkNumber - 1).unmark();
                        System.out.println("    OK, I've marked this task as not done yet:");
                        System.out.println("        " + list.get(unmarkNumber - 1).toString());
                        System.out.println(line);
                        break;
                    case TODO:
                        try {
                            String todoDescription = userInput.substring(5);
                            if (todoDescription.isEmpty()) {
                                throw new BongException("A todo needs a description!");
                            }
                            list.add(new Todo(todoDescription));
                            System.out.println("    Got it. I've added this task:");
                            System.out.println("        " + list.get(list.size() - 1).toString());
                            System.out.println("    " + "Now you have " + list.size() + " tasks in the list.");
                            System.out.println(line);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("A todo needs a description!");
                        }
                    case DEADLINE:
                        try {
                            String[] deadlineParts = userInput.split(" /by ");
                            String deadlineDescription = deadlineParts[0].substring(9);
                            if (deadlineDescription.isBlank() || deadlineParts[1].isBlank()) {
                                throw new BongException("Looks like your 'deadline' is missing details! Description or deadline cannot be empty.");
                            }
                            list.add(new Deadline(deadlineDescription, deadlineParts[1]));
                            System.out.println("    Got it. I've added this task:");
                            System.out.println("        " + list.get(list.size() - 1).toString());
                            System.out.println("    " + "Now you have " + list.size() + " tasks in the list.");
                            System.out.println(line);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("Looks like your 'deadline' is missing details! Description or deadline cannot be empty.");
                        }
                    case EVENT:
                        try {
                            String regex = " /from | /to ";
                            String[] eventParts = userInput.split(regex);
                            String description = eventParts[0].substring(6);
                            if (description.isBlank() || eventParts[1].isBlank() || eventParts[2].isBlank()) {
                                throw new BongException("Looks like your 'event' is missing details! Description, start time and end time cannot be empty.");
                            }
                            list.add(new Event(description, eventParts[1], eventParts[2]));
                            System.out.println("    Got it. I've added this task:");
                            System.out.println("        " + list.get(list.size() - 1).toString());
                            System.out.println("    " + "Now you have " + list.size() + " tasks in the list.");
                            System.out.println(line);
                            break;
                        } catch (StringIndexOutOfBoundsException e) {
                            throw new BongException("Looks like your 'event' is missing details! Description, start time and end time cannot be empty.");
                        }
                    case DELETE:
                        String taskNumberString = userInput.substring(7);
                        int taskNumber = Integer.parseInt(taskNumberString);
                        if (taskNumber > list.size()) {
                            throw new BongException("You do not have this many tasks in your list!");
                        }
                        Task removedTask = list.remove(taskNumber - 1);
                        System.out.println("    Noted. I've removed this task:");
                        System.out.println("        " + removedTask);
                        System.out.println("    Now you have " + list.size() + " tasks in the list.");
                        System.out.println(line);
                        break;
                    case UNKNOWN:
                    default:
                        throw new BongException("    Hmm, I dont understand that command.\n    Please try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete' or 'bye'.");
                }
            } catch (BongException e) { 
                System.out.println(e.getMessage());
                System.out.println(line);
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println(line);
            }
        }

        scanner.close();
    }
}
