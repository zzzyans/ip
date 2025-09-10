package bong.parser;

import bong.Bong;
import bong.exception.BongException;

/**
 * Deals with making sense of the user command.
 * The Parser class is responsible for interpreting raw user input strings
 * into a structured command object that the Bong application can execute.
 */
public class Parser {
    private static final String DEADLINE_DELIM = " /by ";
    private static final String EVENT_DELIM_REGEX = " /from | /to ";

    /**
     * Encapsulates the result of parsing a user command.
     * It contains the command type and its specific arguments (description,
     * deadline time, event start/end times, or task number).
     */
    public static class ParsedCommand {
        public Bong.Command command;
        public String description; // For Todo, Deadline, Event
        public String deadline; // for Deadline
        public String eventStart; // For Event
        public String eventEnd; // For Event
        public int taskNumber; // For Mark, Unmark, Delete
        public String keyword;

        /**
         * Constructor for commands without specific arguments.
         * 
         * @param command The type of command.
         */
        public ParsedCommand(Bong.Command command) {
            this.command = command;
        }

        /**
         * Constructor for Todo command.
         * 
         * @param command The type of command.
         * @param description The description for the task.
         */
        public ParsedCommand(Bong.Command command, String description) {
            this.command = command;
            this.description = description;
        }

        /**
         * Constructor for Deadline command.
         * 
         * @param command The type of command.
         * @param description The description for the task.
         * @param deadline The deadline time string.
         */
        public ParsedCommand(Bong.Command command, String description, String deadline) {
            this.command = command;
            this.description = description;
            this.deadline = deadline;
        }

        /**
         * Constructor for Event command.
         * 
         * @param command The type of command.
         * @param description The description for the task.
         * @param start The start time string.
         * @param end The end time string.
         */
        public ParsedCommand(Bong.Command command, String description, String start, String end) {
            this.command = command;
            this.description = description;
            this.eventStart = start;
            this.eventEnd = end;
        }

        /**
         * Constructor for commands requiring a task number.
         * 
         * @param command The type of command.
         * @param taskNumber The 1-based index of the task.
         */
        public ParsedCommand(Bong.Command command, int taskNumber) {
            this.command = command;
            this.taskNumber = taskNumber;
        }

        /*
         * Constructor for Find command.
         *
         * @param command The type of command.
         * @param keyword The keyword to search for.
         */
        public ParsedCommand(Bong.Command command, String keyword, boolean isFind) {
            this.command = command;
            this.keyword = keyword;
        }
    }

    /**
    * Parses the full user command string and extracts the command type and its arguments.
    * 
    * @param fullCommand The complete user input string.
    * @return A ParsedCommand object containing the command type and its extracted arguments.
    * @throws BongException If the command is not understood or its format is invalid/incomplete.
    */
    public static ParsedCommand parse(String fullCommand) throws BongException {
        String[] inputParts = fullCommand.trim().split(" ", 2);
        String commandWord = inputParts[0].toUpperCase();
        String arguments = inputParts.length > 1 ? inputParts[1].trim() : "";

        Bong.Command command;

        try {
            command = Bong.Command.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            throw new BongException("    Hmm, I don't understand that command.\n"
                + "    Please try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete' or 'bye'.");
        }

        switch (command) {
            case LIST:
                return new ParsedCommand(command);
            
            case MARK:
            case UNMARK:
            case DELETE:
                if (arguments.isEmpty()) {
                    throw new BongException("   The task number cannot be empty for "
                            + commandWord.toLowerCase() + " command.");
                }
                try {
                    int taskNumber = Integer.parseInt(arguments);
                    return new ParsedCommand(command, taskNumber);
                } catch (NumberFormatException e) {
                    throw new BongException("    The task number provided is invalid." +
                            " Please enter a valid number.");
                }

            case TODO:
                if (arguments.isEmpty()) {
                    throw new BongException("    A todo needs a description!");
                }
                return new ParsedCommand(command, arguments);
            
            case DEADLINE:
                String[] deadlineParts = arguments.split(DEADLINE_DELIM, 2);
                if (deadlineParts.length < 2) {
                    throw new BongException("Looks like your 'deadline' is missing details!" +
                            " Try 'deadline <description> /by <yyyy-MM-dd HHmm>.'");
                }
                String deadlineDescription = deadlineParts[0].trim();
                String deadlineTime = deadlineParts[1].trim();
                if (deadlineDescription.isEmpty() || deadlineTime.isEmpty()) {
                    throw new BongException("Looks like your 'deadline' is missing details!" +
                            " Try 'deadline <description> /by <yyyy-MM-dd HHmm>.'");
                }
                return new ParsedCommand(command, deadlineDescription, deadlineTime);
            
            case EVENT:
                String[] eventParts = arguments.split(EVENT_DELIM_REGEX, 3);
                if (eventParts.length < 3) {
                    throw new BongException("Looks like your 'event' is missing details!" +
                            " Try 'event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>.'");
                }
                String eventDescription = eventParts[0].trim();
                String eventStartTime = eventParts[1].trim();
                String eventEndTime = eventParts[2].trim();
                if (eventDescription.isEmpty() || eventStartTime.isEmpty() || eventEndTime.isEmpty()) {
                    throw new BongException("Looks like your 'event' is missing details!" +
                            " Try 'event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>.'");
                }
                return new ParsedCommand(command, eventDescription, eventStartTime, eventEndTime);
            case FIND:
                if (arguments.isEmpty()) {
                    throw new BongException("    The 'find' command needs a keyword to search for!");
                }
                return new ParsedCommand(command, arguments, true);
            default:
                throw new BongException("   An unexpected command type was encountered during parsing.");
        }
    }
}
