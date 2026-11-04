package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.exception.BackendException;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class RentEntityTest
{
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
        LandlordEntity testLndLrd = new LandlordEntity();

        RoommateEntity nullrmteNtt = new RoommateEntity();

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
}
