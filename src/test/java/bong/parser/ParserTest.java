package bong.parser;

import bong.Bong;
import bong.exception.BongException;
import bong.parser.Parser.ParsedCommand;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    @Test
    void parse_listCommand_returnsCorrectCommandType() throws BongException {
        ParsedCommand command = Parser.parse("list");
        assertEquals(Bong.Command.LIST, command.command);
    }

    @Test
    void parse_todoCommand_returnsCorrectDescription() throws BongException {
        ParsedCommand command = Parser.parse("todo read book");
        assertEquals(Bong.Command.TODO, command.command);
        assertEquals("read book", command.description);
    }

    @Test
    void parse_todoCommandEmptyDescription_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("todo "));
        assertEquals("A todo needs a description!", exception.getMessage());
    }

    @Test
    void parse_deadlineCommand_returnsCorrectDetails() throws BongException {
        ParsedCommand command = Parser.parse("deadline return book /by 2025-10-16 1800");
        assertEquals(Bong.Command.DEADLINE, command.command);
        assertEquals("return book", command.description);
        assertEquals("2025-10-16 1800", command.deadline);
    }

    @Test
    void parse_deadlineCommandMissingBy_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("deadline return book"));
        assertTrue(exception.getMessage().contains("Looks like your 'deadline' is missing details!"));
    }

    @Test
    void parse_deadlineCommandEmptyDescription_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("deadline /by 2025-10-16 1800"));
        assertTrue(exception.getMessage().contains("Looks like your 'deadline' is missing details!"));
    }

    @Test
    void parse_deadlineCommandEmptyTime_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("deadline return book /by "));
        assertTrue(exception.getMessage().contains("Looks like your 'deadline' is missing details!"));
    }

    @Test
    void parse_eventCommand_returnsCorrectDetails() throws BongException {
        ParsedCommand command = Parser.parse("event project meeting /from 2025-09-30 1200 /to 2025-09-30 1500");
        assertEquals(Bong.Command.EVENT, command.command);
        assertEquals("project meeting", command.description);
        assertEquals("2025-09-30 1200", command.eventStart);
        assertEquals("2025-09-30 1500", command.eventEnd);
    }

    @Test
    void parse_eventCommandMissingFrom_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("event project meeting /to 2025-09-30 1500"));
        assertTrue(exception.getMessage().contains("Looks like your 'event' is missing details!"));
    }

    @Test
    void parse_eventCommandMissingTo_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("event project meeting /from 2025-09-30 1200"));
        assertTrue(exception.getMessage().contains("Looks like your 'event' is missing details!"));
    }

    @Test
    void parse_eventCommandEmptyDescription_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("event /from 2025-09-30 1200 /to 2025-09-30 1500"));
        assertTrue(exception.getMessage().contains("Looks like your 'event' is missing details!"));
    }

    @Test
    void parse_markCommand_returnsCorrectTaskNumber() throws BongException {
        ParsedCommand command = Parser.parse("mark 1");
        assertEquals(Bong.Command.MARK, command.command);
        assertEquals(1, command.taskNumber);
    }

    @Test
    void parse_markCommandNonNumeric_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("mark abc"));
        assertEquals("The task number provided is invalid. Please enter a valid number.", exception.getMessage());
    }

    @Test
    void parse_markCommandEmptyNumber_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("mark "));
        assertEquals("The task number cannot be empty for mark command.", exception.getMessage());
    }

    @Test
    void parse_unmarkCommand_returnsCorrectTaskNumber() throws BongException {
        ParsedCommand command = Parser.parse("unmark 5");
        assertEquals(Bong.Command.UNMARK, command.command);
        assertEquals(5, command.taskNumber);
    }

    @Test
    void parse_deleteCommand_returnsCorrectTaskNumber() throws BongException {
        ParsedCommand command = Parser.parse("delete 2");
        assertEquals(Bong.Command.DELETE, command.command);
        assertEquals(2, command.taskNumber);
    }

    @Test
    void parse_unknownCommand_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("abcde command"));
        assertTrue(exception.getMessage().contains("I don't understand that command."));
    }

    @Test
    void parse_emptyInput_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse(""));
        assertTrue(exception.getMessage().contains("I don't understand that command."));
    }
}
