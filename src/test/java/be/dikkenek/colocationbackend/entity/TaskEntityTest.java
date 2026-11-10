package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskEntityTest {

    @Test
    void test_set_name() throws BackendException {
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setName("test");
        assertEquals("test", taskEntity.getName());

        assertThrows(BackendException.class, () -> taskEntity.setName(""));
    }
}
