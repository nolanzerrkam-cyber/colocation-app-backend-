package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RoommateEntityTest {
    @Test
    void test_roommate_add_task() throws BackendException {
        RoommateEntity roommateEntity1 = new RoommateEntity("email1@email.com", "password", "firstname1", "lastname1", "0404040404");
        RoommateEntity roommateEntity2 = new RoommateEntity("email2@email.com", "password", "firstname2", "lastname2", "0404040404");

        TaskEntity taskEntity1 = new TaskEntity("n", "d", false, new Date(), roommateEntity1);
        TaskEntity taskEntity2 = new TaskEntity("n", "d", false, new Date(), roommateEntity2);

        roommateEntity1.addTask(taskEntity1);
        assertTrue(roommateEntity1.getTasks().contains(taskEntity1));

        // Test if the task is already inside the list, it won't add it one more time
        roommateEntity1.addTask(taskEntity1);
        assertEquals(1, roommateEntity1.getTasks().size());

        assertThrows(BackendException.class, () -> roommateEntity1.addTask(null));
        assertThrows(BackendException.class, () -> roommateEntity1.addTask(taskEntity2));
    }
}
