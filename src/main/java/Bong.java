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
            } else {
                list[count] = new Task(userInput);
                count++;
                System.out.println("    added: " + userInput);
                System.out.println(line);
            }
        }

        System.out.println("    Bye. Hope to see you again soon!");

        scanner.close();
    }
}
