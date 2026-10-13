package be.dikkenek.colocationbackend.test.api.task;

import be.dikkenek.colocationbackend.entity.TaskEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskEntityTest {

    @Test
    void test_set_name() {
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setName("test");
        assertEquals("test", taskEntity.getName());

        assertThrows(IllegalArgumentException.class, () -> taskEntity.setName(""));
    }
}
