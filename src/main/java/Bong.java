import java.util.Scanner;

public class Bong {
    public static void main(String[] args) {
        String line = "------------------------------";

        System.out.println("    Hello! I'm Bong!\n    What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        String userInput;

        String[] list = new String[100];
        int count = 0;

        while (true) {
            userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                break;
            } else if (userInput.equals("list")) {
                int listNumber = 1;
                for (int i = 0; i < count; i++) {
                    System.out.println("    " + listNumber + ". " + list[i]);
                    listNumber++;
                }
                System.out.println(line);
            } else {
                list[count] = userInput;
                count++;
                System.out.println("    added: " + userInput);
                System.out.println(line);
            }
        }

        System.out.println("    Bye. Hope to see you again soon!");

        scanner.close();
    }
}
