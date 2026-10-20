package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseEntityTest {

    @Test
    // NEW : I changed the name of test_price_valid to test_constructor_rejects_invalid_values.
    void test_constructor_rejects_invalid_values() throws BackendException {
        ExpenseEntity expense = new ExpenseEntity("Courses", 24.50f, null);

        assertEquals(24.50f, expense.getPrice());

        // I'm testing if the price is equals to zero or negative if it throws an error.
        // NEW : I modified the unit test. Since the entity is now valid, I check too if the label is not null or equals to ""
        assertThrows(BackendException.class, () -> new ExpenseEntity("Courses", 0f, null));
        assertThrows(BackendException.class, () -> new ExpenseEntity("Courses", -5f, null));
        assertThrows(BackendException.class, () -> new ExpenseEntity(null, 1f, null));
        assertThrows(BackendException.class, () -> new ExpenseEntity("", 10f, null));
    }

    // DELETED : test_email_valid, since we are aleardy doing it in the UserEntityTest

    // DELETED : test_constructor_rejects_negative_price : because test_constructor_validation is aleardy covering this case.

    @Test
    void test_getters_return_constructor_values() throws BackendException {
        // Test the constructor
        RoommateEntity roommate = Mockito.mock(RoommateEntity.class);
        ExpenseEntity testExpense = new ExpenseEntity("Courses", 67.67f, roommate);

        assertEquals("Courses", testExpense.getLabel());
        assertEquals(67.67f, testExpense.getPrice());
        // NEW : since we are using New Date now, it means that the date is automaticlly set.
        // I just have to test is the method getDate dosen't return a null value.
        //assertEquals(date,testExpense.getExpenseDate());
        assertNotNull(testExpense.getDate());
        // NEW : testing if we get the same roomate through the object ExpenseEntity and RoomateEntity
        assertSame(roommate, testExpense.getRoomate());
    }

    // DELETED : now that I test directly the date being null in test_getters_return_constructor_values, we don't need this unit test anymore.

    // NOTE ; for now, im not testing values with the variable roomate from the ExpenseEntity since it dosen't have buisness rules for the moment.
}