package bong.parser;

import bong.command.Command;
import bong.command.DeadlineCommand;
import bong.command.EventCommand;
import bong.command.HelpCommand;
import bong.command.ListCommand;
import bong.command.MarkCommand;
import bong.command.TodoCommand;
import bong.exception.BongException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    void parse_listCommand_returnsListCommand() throws BongException {
        Command c = Parser.parse("list");
        assertTrue(c instanceof ListCommand);
    }

    @Test
    void parse_todoCommand_returnsTodoCommandWithDescription() throws BongException {
        Command c = Parser.parse("todo read book");
        assertTrue(c instanceof TodoCommand);
        TodoCommand t = (TodoCommand) c;
        assertEquals("read book", t.getDescription());
    }

    @Test
    void parse_todoCommandEmptyDescription_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("todo "));
        assertTrue(exception.getMessage().contains("A todo needs a description"));
    }

    @Test
    void parse_deadlineCommand_returnsDeadlineCommandWithDetails() throws BongException {
        Command c = Parser.parse("deadline return book /by 2025-10-16 1800");
        assertTrue(c instanceof DeadlineCommand);
        DeadlineCommand d = (DeadlineCommand) c;
        assertEquals("return book", d.getDescription());
        assertEquals("2025-10-16 1800", d.getDeadlineTime());
    }

    @Test
    void parse_eventCommand_returnsEventCommandWithDetails() throws BongException {
        Command c = Parser.parse("event project meeting /from 2025-09-30 1200 /to 2025-09-30 1500");
        assertTrue(c instanceof EventCommand);
        EventCommand e = (EventCommand) c;
        assertEquals("project meeting", e.getDescription());
        assertEquals("2025-09-30 1200", e.getStartTime());
        assertEquals("2025-09-30 1500", e.getEndTime());
    }

    @Test
    void parse_markCommand_returnsMarkCommandWithTaskNumber() throws BongException {
        Command c = Parser.parse("mark 1");
        assertTrue(c instanceof MarkCommand);
        MarkCommand m = (MarkCommand) c;
        assertEquals(1, m.getTaskNumber());
    }

    @Test
    void parse_markCommandNonNumeric_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("mark abc"));
        assertTrue(exception.getMessage().contains("The task number provided is invalid"));
    }

    @Test
    void parse_unknownCommand_throwsBongException() {
        Exception exception = assertThrows(BongException.class, () -> Parser.parse("abcde command"));
        assertTrue(exception.getMessage().toLowerCase().contains("don't understand") ||
                exception.getMessage().toLowerCase().contains("i don't understand"));
    }

    @Test
    void parse_helpCommand_returnsHelpCommand() throws BongException {
        Command c = Parser.parse("help");
        assertTrue(c instanceof HelpCommand);
        HelpCommand h = (HelpCommand) c;
        // execute the command to get the help string and assert some keywords are present
        String help = h.execute(null, null, null);
        assertTrue(help.contains("todo"));
        assertTrue(help.contains("deadline"));
        assertTrue(help.contains("event"));
        assertTrue(help.contains("snooze"));
    }
}