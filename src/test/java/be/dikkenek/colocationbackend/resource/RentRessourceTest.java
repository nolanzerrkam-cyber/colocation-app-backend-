package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.RentDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.RentResponseDto;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentRessourceTest
{
    @Mock
    private RentDao rentDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private RentRessource rentRessource;

    @Test
    void userEntity_isNull() throws BackendException
    {
        // Given - When
        Response resp = rentRessource.getRent(null);

        // Then
        Assertions.assertEquals(404, resp.getStatus());
    }
}
