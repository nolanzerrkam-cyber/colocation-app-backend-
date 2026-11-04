package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RentEntityTest
{
    // A date in X days (negative = in the past)
    private Date inDays(int days)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    // setDeadline refuses a date in the past, so to simulate a deadline that is over,
    // we write directly in the field. Only for tests !
    private void forceDeadline(RentEntity rent, Date date) throws Exception
    {
        Field f = RentEntity.class.getDeclaredField("deadline");
        f.setAccessible(true);
        f.set(rent, date);
    }

    private RoommateEntity newRoommate(String email) throws BackendException
    {
        return new RoommateEntity(email, "password", "first", "last", "0404040404");
    }

    @Test
    void rentNtt_InvalidValuesConstructor() throws BackendException
    {
        // c is correct
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 20);

        // b is not correct : below two weeks
        Calendar b = Calendar.getInstance();
        b.add(Calendar.DAY_OF_MONTH, 10);

        // a is not correct : above 1 month
        Calendar a = Calendar.getInstance();
        a.add(Calendar.MONTH, 2);

        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);

        // The landlord is null
        assertThrows(BackendException.class, () -> new RentEntity(1, 800f, c.getTime(), (LandlordEntity)null));
        // The time is :
        // 1) Below at least two weeks later
        // 2) Above 1 month later
        // 3) null
        assertThrows(BackendException.class, () -> new RentEntity(2, 800f, b.getTime(), lndLrd));
        assertThrows(BackendException.class, () -> new RentEntity(3, 800f, a.getTime(), lndLrd));
        assertThrows(BackendException.class, () -> new RentEntity(30, 800f, null, lndLrd));
        // For the price, testing a price = 0 and a negative price and with a NaN.
        assertThrows(BackendException.class, () -> new RentEntity(4, 0f, c.getTime(), lndLrd));
        assertThrows(BackendException.class, () -> new RentEntity(5, -1000f, c.getTime(), lndLrd));
        assertThrows(BackendException.class, () -> new RentEntity(10, Float.NaN, c.getTime(), lndLrd));
        // Testing the second constructor : null roommate
        assertThrows(BackendException.class, () -> new RentEntity(6, 800f, c.getTime(), lndLrd, (RoommateEntity)null));
    }

    @Test
    void rentNtt_ValidConstructor() throws BackendException
    {
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        Date deadline = inDays(20);

        RentEntity rent = new RentEntity(1, 800f, deadline, lndLrd);

        assertEquals(1, rent.getId());
        assertEquals(800f, rent.getPrice());
        assertEquals(deadline, rent.getDeadline());
        assertEquals(lndLrd, rent.getLandlord());
        // No roommate yet
        assertTrue(rent.getRoommatePaymentStatus().isEmpty());

        // Second constructor : with a roommate, it has one status, not paid
        RentEntity rentWithRoommate = new RentEntity(2, 800f, deadline, lndLrd, newRoommate("a@email.com"));
        assertEquals(1, rentWithRoommate.getRoommatePaymentStatus().size());
        assertFalse(rentWithRoommate.getRoommatePaymentStatus().get(0).isPaid());
    }

    @Test
    void rentNtt_SetPrice() throws BackendException
    {
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rent = new RentEntity(1, 800f, inDays(20), lndLrd);

        // A correct price
        rent.setPrice(900f);
        assertEquals(900f, rent.getPrice());

        // An incorrect price : 0, negative and NaN. The old price stays.
        assertThrows(BackendException.class, () -> rent.setPrice(0f));
        assertThrows(BackendException.class, () -> rent.setPrice(-50f));
        assertThrows(BackendException.class, () -> rent.setPrice(Float.NaN));
        assertEquals(900f, rent.getPrice());
    }

    @Test
    void rentNtt_SetDeadline() throws BackendException
    {
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rent = new RentEntity(1, 800f, inDays(20), lndLrd);

        // A correct deadline
        Date newDeadline = inDays(25);
        rent.setDeadline(newDeadline);
        assertEquals(newDeadline, rent.getDeadline());

        // Incorrect : null, in the past, below two weeks, above one month. The old deadline stays.
        assertThrows(BackendException.class, () -> rent.setDeadline(null));
        assertThrows(BackendException.class, () -> rent.setDeadline(inDays(-5)));
        assertThrows(BackendException.class, () -> rent.setDeadline(inDays(10)));
        assertThrows(BackendException.class, () -> rent.setDeadline(inDays(45)));
        assertEquals(newDeadline, rent.getDeadline());
    }

    @Test
    void rentNtt_AddAndRemoveRoommate() throws BackendException
    {
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rent = new RentEntity(1, 800f, inDays(20), lndLrd);
        RoommateEntity rmte = newRoommate("a@email.com");

        // Adding a roommate : one status, not paid
        rent.addRoomate(rmte);
        assertEquals(1, rent.getRoommatePaymentStatus().size());
        assertFalse(rent.getRoommatePaymentStatus().get(0).isPaid());

        // Adding the same roommate twice does not add a second status
        rent.addRoomate(rmte);
        assertEquals(1, rent.getRoommatePaymentStatus().size());

        // Removing the roommate
        rent.removeRoommate(rmte);
        assertTrue(rent.getRoommatePaymentStatus().isEmpty());
    }

    @Test
    void rentNtt_ResetPayement() throws Exception
    {
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rent = new RentEntity(1, 800f, inDays(20), lndLrd);
        rent.addRoomate(newRoommate("a@email.com"));
        rent.getRoommatePaymentStatus().get(0).setPaid(true);

        // The deadline is not over yet : exception, and the roommate is still paid
        assertThrows(BackendException.class, () -> rent.resetPayement());
        assertTrue(rent.getRoommatePaymentStatus().get(0).isPaid());

        // The deadline is over since 10 days : nobody paid anymore, and the new deadline is in the future
        forceDeadline(rent, inDays(-10));
        rent.resetPayement();
        assertFalse(rent.getRoommatePaymentStatus().get(0).isPaid());
        assertTrue(rent.getDeadline().after(new Date()));

        // The deadline is over since more than a month : the loop has to bring it in the future
        forceDeadline(rent, inDays(-70));
        rent.resetPayement();
        assertTrue(rent.getDeadline().after(new Date()));
    }

    @Test
    void rentNtt_AmountPerRoommate() throws BackendException
    {
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rent = new RentEntity(1, 800f, inDays(20), lndLrd);

        // No roommate : we can't divide the rent
        assertThrows(BackendException.class, () -> rent.getAmountPerRoommate());

        // 3 roommates : 800 / 3 = 266.666... rounded to the hundredth
        rent.addRoomate(newRoommate("a@email.com"));
        rent.addRoomate(newRoommate("b@email.com"));
        rent.addRoomate(newRoommate("c@email.com"));
        assertEquals(266.67f, rent.getAmountPerRoommate(), 0.001f);
    }
}