package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlatshareEntityTest {

    @Test
    void test_getters_return_default_values() {
        FlatshareEntity flatshareEntity = new FlatshareEntity();

        assertEquals(0, flatshareEntity.getId_flatshare());
        assertNull(flatshareEntity.getLandlord());
        assertNull(flatshareEntity.getRent());
        assertNotNull(flatshareEntity.getRoommates());
        assertTrue(flatshareEntity.getRoommates().isEmpty());
        assertNotNull(flatshareEntity.getTasks());
        assertTrue(flatshareEntity.getTasks().isEmpty());
        assertNotNull(flatshareEntity.getShoppingQuantityItems());
        assertTrue(flatshareEntity.getShoppingQuantityItems().isEmpty());
        assertNotNull(flatshareEntity.getExpenses());
        assertTrue(flatshareEntity.getExpenses().isEmpty());
    }

    @Test
    void test_setters() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        LandlordEntity landlord = new LandlordEntity();
        RentEntity rent = new RentEntity();

        flatshareEntity.setId_flatshare(5);
        flatshareEntity.setLandlord(landlord);
        flatshareEntity.setRent(rent);

        assertAll(
                () -> assertEquals(5, flatshareEntity.getId_flatshare()),
                () -> assertSame(landlord, flatshareEntity.getLandlord()),
                () -> assertSame(flatshareEntity, landlord.getFlatshare()),
                () -> assertSame(rent, flatshareEntity.getRent())
        );
    }

    @Test
    void test_add_and_remove_roommate() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate = new RoommateEntity();

        flatshareEntity.addRoommate(roommate);
        flatshareEntity.addRoommate(roommate);

        assertEquals(1, flatshareEntity.getRoommates().size());
        assertTrue(flatshareEntity.getRoommates().contains(roommate));
        assertSame(flatshareEntity, roommate.getFlatshare());

        flatshareEntity.removeRoommate(roommate);
        assertTrue(flatshareEntity.getRoommates().isEmpty());
    }

    @Test
    void test_add_and_remove_task() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        TaskEntity task = new TaskEntity();

        flatshareEntity.addTask(task);
        flatshareEntity.addTask(task);

        assertEquals(1, flatshareEntity.getTasks().size());
        assertTrue(flatshareEntity.getTasks().contains(task));

        flatshareEntity.removeTask(task);
        assertTrue(flatshareEntity.getTasks().isEmpty());
    }

    @Test
    void test_add_and_remove_item() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        ShoppingQuantityEntity item = new ShoppingQuantityEntity();

        flatshareEntity.addItem(item);
        flatshareEntity.addItem(item);

        assertEquals(1, flatshareEntity.getShoppingQuantityItems().size());
        assertTrue(flatshareEntity.getShoppingQuantityItems().contains(item));

        flatshareEntity.removeItem(item);
        assertTrue(flatshareEntity.getShoppingQuantityItems().isEmpty());
    }

    @Test
    void test_add_and_remove_expense() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        ExpenseEntity expense = mock(ExpenseEntity.class);

        flatshareEntity.addExpense(expense);
        flatshareEntity.addExpense(expense);

        assertEquals(1, flatshareEntity.getExpenses().size());
        assertTrue(flatshareEntity.getExpenses().contains(expense));

        flatshareEntity.removeExpense(expense);
        assertTrue(flatshareEntity.getExpenses().isEmpty());
    }

    @Test
    void test_add_rejects_null() {
        FlatshareEntity flatshareEntity = new FlatshareEntity();

        assertThrows(BackendException.class, () -> flatshareEntity.addRoommate(null));
        assertThrows(BackendException.class, () -> flatshareEntity.addTask(null));
        assertThrows(BackendException.class, () -> flatshareEntity.addItem(null));
        assertThrows(BackendException.class, () -> flatshareEntity.addExpense(null));

        assertTrue(flatshareEntity.getRoommates().isEmpty());
        assertTrue(flatshareEntity.getTasks().isEmpty());
        assertTrue(flatshareEntity.getShoppingQuantityItems().isEmpty());
        assertTrue(flatshareEntity.getExpenses().isEmpty());
    }

    @Test
    void test_get_total_expenses() throws BackendException {
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate1 = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        RoommateEntity roommate2 = new RoommateEntity("email2@email.com", "password", "b", "b", "0474998235", flatshareEntity);

        assertEquals(0.0f, flatshareEntity.getTotalExpenses());

        ExpenseEntity expense1 = mock(ExpenseEntity.class);
        when(expense1.getRoommate()).thenReturn(roommate1);
        when(expense1.getPrice()).thenReturn(50f);

        ExpenseEntity expense2 = mock(ExpenseEntity.class);
        when(expense2.getRoommate()).thenReturn(roommate1);
        when(expense2.getPrice()).thenReturn(25f);

        ExpenseEntity expense3 = mock(ExpenseEntity.class);
        when(expense3.getRoommate()).thenReturn(roommate2);
        when(expense3.getPrice()).thenReturn(10f);

        flatshareEntity.addExpense(expense1);
        flatshareEntity.addExpense(expense2);
        flatshareEntity.addExpense(expense3);

        assertEquals(85f, flatshareEntity.getTotalExpenses());
        assertEquals(75f, roommate1.getTotalExpenses());
        assertEquals(10f, roommate2.getTotalExpenses());
    }
}