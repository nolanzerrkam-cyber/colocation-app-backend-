package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.RentDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.dto.RentUpdateDto;
import be.dikkenek.colocationbackend.entity.LandlordEntity;
import be.dikkenek.colocationbackend.entity.RentEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.listeners.MockitoListener;

import java.util.Calendar;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RentRessourceTest
{
    @Mock
    private RentDao rentDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private RentRessource rentRessource;

    // --------------------- .../GET only ----------------------- //
    @Test
    void userEntity_isNull() throws BackendException
    {
        // Given - When
        Response resp = rentRessource.getRent(null);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void userEntity_notLandlord() throws BackendException
    {
        // Given
        RoommateEntity rmteEtt = new RoommateEntity("roommate@account.com", "password", "room", "mate", "012345678");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(rmteEtt));

        // When
        Response resp = rentRessource.getRent(rmteEtt.getEmail());

        // Then
        Assertions.assertEquals(403, resp.getStatus());
    }

    @Test
    void landlord_noRents() throws BackendException
    {
        // Given
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(lndLrd));

        // When

        Response resp = rentRessource.getRent(lndLrd.getEmail());

        // Then

        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void getRent_Success() throws BackendException
    {
        // Given
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 20);

        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rntNtt = new RentEntity(1, 800f, c.getTime(), lndLrd);
        lndLrd.setRent(rntNtt);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(lndLrd));

        // When

        Response resp = rentRessource.getRent(lndLrd.getEmail());

        // Then

        Assertions.assertEquals(200, resp.getStatus());
    }

    // ------------------ /PUT only ----------------------- //

    @Test
    void updateRent_dtoNotValid() throws BackendException
    {
        // Given - when
        Response resp = rentRessource.updateRent("landlord@account.com", null);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void updateRent_userEntityNull() throws BackendException
    {
        // Given
        RentUpdateDto dto = new RentUpdateDto();
        dto.setPrice(200f);

        // When
        Response resp = rentRessource.updateRent(null, dto);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void updateRent_InvalidPrice() throws BackendException
    {
        // Given
        RentUpdateDto dto = new RentUpdateDto();
        RentUpdateDto dto2 = new RentUpdateDto();
        dto.setPrice(0);
        dto2.setPrice(-800);

        // When
        Response resp = rentRessource.updateRent("landlord@account.com", dto);
        Response resp2 = rentRessource.updateRent("landlord@account.com", dto2);

        // Then
        Assertions.assertEquals(400, resp.getStatus());
        Assertions.assertEquals(400, resp2.getStatus());
    }

    @Test
    void updateRent_NotLandlord() throws BackendException
    {
        // Given
        RentUpdateDto dto = new RentUpdateDto();
        dto.setPrice(800f);
        RoommateEntity rmteNtt = new RoommateEntity("roommate@account.com", "password", "room", "mate", "012345678");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(rmteNtt));

        // When
        Response resp = rentRessource.updateRent(rmteNtt.getEmail(), dto);

        // Then
        Assertions.assertEquals(403, resp.getStatus());
    }

    @Test
    void updateRent_rentNull() throws BackendException
    {
        // Given
        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentUpdateDto dto = new RentUpdateDto(); dto.setPrice(800f);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(lndLrd));

        // When
        Response resp = rentRessource.updateRent(lndLrd.getEmail(), dto);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }

    @Test
    void updateRent_Success() throws BackendException
    {
        // Given
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 20);

        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rntNtt = new RentEntity(1, 800f, c.getTime(), lndLrd);
        lndLrd.setRent(rntNtt);

        RentUpdateDto dto = new RentUpdateDto(); dto.setPrice(1200f);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(lndLrd));
        Mockito.when(rentDao.update(Mockito.any(RentEntity.class))).thenReturn(true);

        // When
        Response resp = rentRessource.updateRent(lndLrd.getEmail(), dto);

        // Then

        Assertions.assertEquals(200, resp.getStatus());
    }

    @Test
    void updateRent_Failed() throws BackendException
    {
        // Given
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 20);

        LandlordEntity lndLrd = new LandlordEntity("landlord@account.com", "password", "land", "lord", "012345678", null);
        RentEntity rntNtt = new RentEntity(1, 800f, c.getTime(), lndLrd);
        lndLrd.setRent(rntNtt);

        RentUpdateDto dto = new RentUpdateDto(); dto.setPrice(1200f);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(lndLrd));
        Mockito.when(rentDao.update(Mockito.any(RentEntity.class))).thenReturn(false);

        // When
        Response resp = rentRessource.updateRent(lndLrd.getEmail(), dto);

        // Then

        Assertions.assertEquals(500, resp.getStatus());
    }


}
