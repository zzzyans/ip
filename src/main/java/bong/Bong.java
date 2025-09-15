package bong;

/**
 * The entry point for the console version of the Bong task application.
 * It instantiates BongCore and starts its console execution.
 */
public class Bong {

    /**
     * Main method to launch the console Bong application.
     * Creates an instance of BongCore and runs its console interface.
     * 
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        BongCore bongCore = new BongCore();
        bongCore.runConsole();
    }
}
