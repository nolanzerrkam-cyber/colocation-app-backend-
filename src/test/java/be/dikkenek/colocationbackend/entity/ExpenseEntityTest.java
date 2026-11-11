package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseEntityTest {

    @Test
    void test_constructor_rejects_invalid_values() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommateEntity = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        ExpenseEntity expense = new ExpenseEntity("Courses", 24.50f, roommateEntity);

        assertEquals(24.50f, expense.getPrice());

        // I'm testing if the price is equals to zero or negative if it throws an error.
        // Since the entity is now valid, I check too if the label is not null or equals to ""
        assertThrows(BackendException.class, () -> new ExpenseEntity("Courses", 0f, roommateEntity));
        assertThrows(BackendException.class, () -> new ExpenseEntity("Courses", -5f, roommateEntity));
        assertThrows(BackendException.class, () -> new ExpenseEntity(null, 1f, roommateEntity));
        assertThrows(BackendException.class, () -> new ExpenseEntity("", 10f, roommateEntity));
        assertThrows(BackendException.class, () -> new ExpenseEntity("aa", 10f, null));
    }

    @Test
    void test_getters_return_constructor_values() throws BackendException
    {
        // Test the constructor
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        ExpenseEntity testExpense = new ExpenseEntity("Courses", 67.67f, roommate);

        assertEquals("Courses", testExpense.getLabel());
        assertEquals(67.67f, testExpense.getPrice());

        // since we are using New Date now, it means that the date is automaticlly set.
        // I just have to test is the method getDate dosen't return a null value.
        //assertEquals(date,testExpense.getExpenseDate());

        assertNotNull(testExpense.getDate());

        // testing if we get the same roomate through the object ExpenseEntity and RoomateEntity
        assertSame(roommate, testExpense.getRoommate());
    }
}