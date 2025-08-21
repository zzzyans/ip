import java.util.Scanner;

public class Bong {
    public static void main(String[] args) {
        String line = "------------------------------";

        System.out.println("    Hello! I'm Bong!\n    What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                break;
            } else {
                System.out.println("    " + userInput);
                System.out.println(line);
            }
        }

        System.out.println("    Bye. Hope to see you again soon!");

        scanner.close();
    }
}
