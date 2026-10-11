package be.dikkenek.colocationbackend.test.api.expense;

import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    void test_set_email()
    {
        ExpenseEntity exp = new ExpenseEntity();

        exp.setEmail("test@test.com");
        assertEquals("test@test.com", exp.getEmail());

        exp.setEmail("nolan.mertens@condorcet.com");
        assertEquals("nolan.mertens@condorcet.com", exp.getEmail());

        assertThrows(IllegalArgumentException.class, () -> exp.setEmail(null));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail(""));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("this-is-not-a-email"));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("name@"));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("@name.com"));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("name@domain"));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("name@domain."));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("name@domain.c"));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("name@dom@ain.c"));
        assertThrows(IllegalArgumentException.class, () -> exp.setEmail("name @dom@ain.com"));
    }

    @Test
    void test_constructor_rejects_negative_price() {
        assertThrows(IllegalArgumentException.class,// And here im testing if the constructor does accept or not a negative price
                () -> new ExpenseEntity("Courses", new BigDecimal("-1"), LocalDateTime.now(), "test@test.com"));

    }

    @Test
    void test_constructor_valid()
    {
        // Test the constructor
        LocalDateTime date = LocalDateTime.now();
        ExpenseEntity testExpense = new ExpenseEntity("Courses", new BigDecimal(67.00), date, "test@test.com");

        assertEquals("Courses", testExpense.getLibele());
        assertEquals(new BigDecimal(67.00), testExpense.getPriceExpense());
        assertEquals(date,testExpense.getExpenseDate());
        assertEquals("test@test.com", testExpense.getEmail());
    }

    @Test
    // This test unit is making sure that
    // if we pass a null value, the program will send an error message !
    void test_set_date()
    {
        ExpenseEntity exp = new ExpenseEntity();

        LocalDateTime date = LocalDateTime.now();
        exp.setExpenseDate(date);
        assertEquals(date, exp.getExpenseDate());

        assertThrows(IllegalArgumentException.class, () -> exp.setExpenseDate(null));
    }
}