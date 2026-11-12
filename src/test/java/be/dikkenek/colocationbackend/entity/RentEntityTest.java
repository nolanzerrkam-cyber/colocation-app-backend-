package be.dikkenek.colocationbackend.entity;


import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RentEntityTest {


    @Test
    public void rent_entity_deadline() throws BackendException {
        RentEntity rentEntity = new RentEntity();

        assertThrows(BackendException.class, () -> rentEntity.setDeadlineDay(0));
        assertThrows(BackendException.class, () -> rentEntity.setDeadlineDay(-1));
        assertThrows(BackendException.class, () -> rentEntity.setDeadlineDay(32));

        rentEntity.setDeadlineDay(23);

        assertEquals(23, rentEntity.getDeadlineDay());
    }

    @Test
    public void rent_entity_amount() throws BackendException {
        RentEntity rentEntity = new RentEntity();

        assertThrows(BackendException.class, () -> rentEntity.setAmount(0));
        assertThrows(BackendException.class, () -> rentEntity.setAmount(-1));

        rentEntity.setAmount(1000);

        assertEquals(1000, rentEntity.getAmount());
    }
}