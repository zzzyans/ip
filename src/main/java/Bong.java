import java.util.Scanner;

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

    public static void main(String[] args) {
        String line = "------------------------------";

        System.out.println("    Hello! I'm Bong!\n    What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        String userInput;

        Task[] list = new Task[100];
        int count = 0;

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                break;
            } else if (userInput.equals("list")) {
                int listNumber = 1;
                System.out.println("    Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println("    " + listNumber + ". " + list[i].toString());
                    listNumber++;
                }
                System.out.println(line);
            } else if (userInput.startsWith("mark ")) {
                String taskNumberString = userInput.substring(5);
                int taskNumber = Integer.parseInt(taskNumberString);
                list[taskNumber - 1].mark();
                System.out.println("    Nice! I've marked this task as done:");
                System.out.println("        " + list[taskNumber - 1].toString());
                System.out.println(line);
            } else if (userInput.startsWith("unmark ")) {
                String taskNumberString = userInput.substring(7);
                int taskNumber = Integer.parseInt(taskNumberString);
                list[taskNumber - 1].unmark();
                System.out.println("    OK, I've marked this task as not done yet:");
                System.out.println("        " + list[taskNumber - 1].toString());
                System.out.println(line);
            } else if (userInput.startsWith("todo ")) {
                list[count] = new Todo(userInput.substring(5));
                System.out.println("    Got it. I've added this task:");
                System.out.println("        " + list[count].toString());
                count++;
                System.out.println("    " + "Now you have " + count + " tasks in the list.");
                System.out.println(line);
            } else if (userInput.startsWith("deadline ")) {
                String[] parts = userInput.split(" /by ");
                list[count] = new Deadline(parts[0].substring(9), parts[1]);
                System.out.println("    Got it. I've added this task:");
                System.out.println("        " + list[count].toString());
                count++;
                System.out.println("    " + "Now you have " + count + " tasks in the list.");
                System.out.println(line);
            } else {
                String regex = " /from | /to ";
                String[] parts = userInput.split(regex);
                list[count] = new Event(parts[0].substring(6), parts[1], parts[2]);
                System.out.println("    Got it. I've added this task:");
                System.out.println("        " + list[count].toString());
                count++;
                System.out.println("    " + "Now you have " + count + " tasks in the list.");
                System.out.println(line);
            }
        }

        System.out.println("    Bye. Hope to see you again soon!");

        scanner.close();
    }
}
