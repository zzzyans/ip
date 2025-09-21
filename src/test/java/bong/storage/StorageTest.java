package bong.storage;

import bong.ui.Ui;
import bong.task.Task;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {
    @Test
    void loadTasks_readsValidFile() throws Exception {
        Path tmp = Files.createTempFile("bong-test", ".txt");
        List<String> lines = List.of(
                "T | 0 | borrow book",
                "D | 0 | return book | 2025-10-16 1200",
                "E | 0 | project meeting | 2025-09-30 1200 | 2025-09-30 1500"
        );
        Files.write(tmp, lines);

        Storage storage = new Storage(tmp.toString());
        Ui ui = new Ui(); // only used for warnings; won't block tests unless you call readCommand
        List<Task> tasks = storage.loadTasks(ui);

        assertEquals(3, tasks.size());
        assertEquals("borrow book", tasks.get(0).getDescription());
        assertEquals("return book", tasks.get(1).getDescription());
        assertEquals("project meeting", tasks.get(2).getDescription());
    }
}