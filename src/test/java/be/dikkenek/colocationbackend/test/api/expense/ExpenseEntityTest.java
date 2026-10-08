package be.dikkenek.colocationbackend.test.api.expense;

import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseEntityTest {

    @Test
    void test_set_price() {
        ExpenseEntity expense = new ExpenseEntity();


        expense.setPriceExpense(new BigDecimal("24.50"));
        assertEquals(new BigDecimal("24.50"), expense.getPriceExpense());

        // Im testing if the price is null, equals to zero or negative if it throws an error.
        assertThrows(IllegalArgumentException.class, () -> expense.setPriceExpense(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> expense.setPriceExpense(new BigDecimal("-5")));
        assertThrows(IllegalArgumentException.class, () -> expense.setPriceExpense(null));

    }

    @Test
    void test_constructor_rejects_negative_price() {
        assertThrows(IllegalArgumentException.class,// And here im testing if the constructor does accept or not a negative price
                () -> new ExpenseEntity("Courses", new BigDecimal("-1"), new Date(), "test@test.com"));

    }

    @Test
    void test_constructor_valid()
    {
        // Test the constructor
        Date date = new Date();
        ExpenseEntity testExpense = new ExpenseEntity("Courses", new BigDecimal(67.00), date, "test@test.com");

        assertEquals("Courses", testExpense.getLibele());
        assertEquals(new BigDecimal(67.00), testExpense.getPriceExpense());
        assertEquals(date,testExpense.getExpenseDate());
        assertEquals("test@test.com", testExpense.getEmail());
    }
}