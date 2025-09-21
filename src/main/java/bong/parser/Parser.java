package bong.parser;

import bong.BongCore.CommandType;

import bong.command.Command;
import bong.command.DeleteCommand;
import bong.command.DeadlineCommand;
import bong.command.EventCommand;
import bong.command.ExitCommand;
import bong.command.FindCommand;
import bong.command.HelpCommand;
import bong.command.ListCommand;
import bong.command.MarkCommand;
import bong.command.SnoozeCommand;
import bong.command.TodoCommand;
import bong.command.UnmarkCommand;
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
    * Parses the full user command string and returns a corresponding Command object.
    * 
    * @param fullCommand The complete user input string.
    * @return A Command object ready for execution.
    * @throws BongException If the command is not recognised or its format is invalid/incomplete.
    */
    public static Command parse(String fullCommand) throws BongException {
        String[] inputParts = fullCommand.trim().split(" ", 2);
        String commandWord = inputParts[0].toUpperCase();
        String arguments = inputParts.length > 1 ? inputParts[1].trim() : "";

        assert commandWord != null : "commandWord should not be null after split";

        CommandType commandEnum;

        try {
            commandEnum = CommandType.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            throw new BongException("Hmm, I don't understand that command.\n"
                + "Please try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete', 'find', or 'bye'.");
        }

        return switch (commandEnum) {
            case LIST -> new ListCommand();
            case BYE -> new ExitCommand();
            case MARK, UNMARK, DELETE -> parseNumberedCommand(commandEnum, arguments, commandWord);
            case TODO -> parseTodoCommand(arguments);
            case DEADLINE -> parseDeadlineCommand(arguments);
            case EVENT -> parseEventCommand(arguments);
            case FIND -> parseFindCommand(arguments);
            case SNOOZE -> parseSnoozeCommand(arguments);
            case HELP -> new HelpCommand();
            default -> throw new BongException("An unexpected command type was encountered during parsing.");
        };
    }

    /**
     * Parses commands that require a task number (MARK, UNMARK, DELETE).
     *
     * @param command The type of command (MARK, UNMARK, DELETE).
     * @param arguments The arguments string containing the task number.
     * @param commandWord The original command word (e.g., "mark", "unmark", "delete").
     * @return A Command object for marking, unmarking, or deleting a task.
     * @throws BongException If the task number is empty or not a valid integer.
     */
    private static Command parseNumberedCommand(
            CommandType command, String arguments, String commandWord) throws BongException {
        if (arguments.isEmpty()) {
            throw new BongException("The task number cannot be empty for " + commandWord.toLowerCase() + " command.");
        }
        try {
            int taskNumber = Integer.parseInt(arguments);
            return switch (command) {
                case MARK -> new MarkCommand(taskNumber);
                case UNMARK -> new UnmarkCommand(taskNumber);
                case DELETE -> new DeleteCommand(taskNumber);
                default -> throw new BongException("Invalid command type for parseNumberedCommand.");
            };
        } catch (NumberFormatException e) {
            throw new BongException("The task number provided is invalid. Please enter a valid number.");
        }
    }

    /**
     * Parses a 'todo' command.
     *
     * @param arguments The arguments string containing the todo description.
     * @return A Command object for adding a todo task.
     * @throws BongException If the description is empty.
     */
    private static Command parseTodoCommand(String arguments) throws BongException {
        if (arguments.isEmpty()) {
            throw new BongException("A todo needs a description!");
        }
        return new TodoCommand(arguments);
    }

    /**
     * Parses a 'deadline' command.
     *
     * @param arguments The arguments string containing the description and deadline time.
     * @return A Command object for adding a deadline task.
     * @throws BongException If the deadline command is missing details.
     */
    private static Command parseDeadlineCommand(String arguments) throws BongException {
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
        return new DeadlineCommand(deadlineDescription, deadlineTime);
    }

    /**
     * Parses an 'event' command.
     *
     * @param arguments The arguments string containing the description, start time, and end time.
     * @return A Command object for adding an event task.
     * @throws BongException If the event command is missing details.
     */
    private static Command parseEventCommand(String arguments) throws BongException {
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
        return new EventCommand(eventDescription, eventStartTime, eventEndTime);
    }

    /**
     * Parses a 'find' command.
     *
     * @param arguments The arguments string containing the keyword to search for.
     * @return A Command object for finding tasks.
     * @throws BongException If the keyword is empty.
     */
    private static Command parseFindCommand(String arguments) throws BongException {
        if (arguments.isEmpty()) {
            throw new BongException("The 'find' command needs a keyword to search for!");
        }
        return new FindCommand(arguments);
    }

    /**
     * Parses a 'snooze' command.
     *
     * @param arguments The arguments string containing the new deadline/start time and end time.
     * @return A SnoozeCommand object for snoozing tasks.
     * @throws BongException If the snooze command is missing details.
     */
    private static Command parseSnoozeCommand(String arguments) throws BongException {
        if (arguments.isEmpty()) {
            throw new BongException("The 'snooze' command needs a new deadline/start time and end time!" +
                    " Try 'snooze <task number> /to <yyyy-MM-dd HHmm> (/end <yyyy-MM-dd HHmm>)'");
        }
        String[] parts = arguments.split(" ", 2);
        if (parts.length < 2) {
            throw new BongException("Try 'snooze <task number> /to <yyyy-MM-dd HHmm> (/end <yyyy-MM-dd HHmm>)'");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new BongException("    The task number provided is invalid. Please enter a valid number.");
        }

        String rest = parts[1].trim();
        String endPart = null;
        String[] endSplit = rest.split(" /end ", 2);
        String toPart = endSplit[0].trim();
        if (endSplit.length == 2) {
            endPart = endSplit[1].trim();
        }

        String toPrefix = "/to ";
        if (!toPart.startsWith(toPrefix)) {
            throw new BongException("Snooze requires '/to <yyyy-MM-dd HHmm>' for new deadline/start time.");
        }
        String newStartString = toPart.substring(toPrefix.length()).trim();
        if (newStartString.isEmpty()) {
            throw new BongException("Snooze requires a non-empty new datetime after /to.");
        }

        return new SnoozeCommand(taskNumber, newStartString, (endPart != null && !endPart.isEmpty()) ? endPart : null);
    }
}
