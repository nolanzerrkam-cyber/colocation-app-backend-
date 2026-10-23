package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
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

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RoommateResourceTest
{
    @Mock
    private UserDao userDao;
    @InjectMocks
    private RoommateResource roommateResource;

    @Test
    void roommate_getAll_valid() throws BackendException {
        // Given
        RoommateEntity roommate1 = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234");
        RoommateEntity roommate2 = new RoommateEntity("email2@email.com", "password", "b", "b", "0472727272");
        List<RoommateEntity> roommates = Arrays.asList(roommate1, roommate2);
        Mockito.when(RoommateEntity.getAll(userDao)).thenReturn(roommates);

        // When
        Response response = roommateResource.getAll();
        List<?> responseEntity = (List<?>) response.getEntity();

        // Then
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(2, responseEntity.size());
        Assertions.assertEquals(roommates, responseEntity);
    }

    @Test
    void roommate_getAll_empty() throws BackendException {
        // Given
        Mockito.when(RoommateEntity.getAll(userDao)).thenReturn(Arrays.asList());

        // When
        Response response = roommateResource.getAll();
        List<?> responseEntity = (List<?>) response.getEntity();

        // Then
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertTrue(responseEntity.isEmpty());
    }

    @Test
    void roommate_getAll_dao_error() throws BackendException {
        // Given
        Mockito.when(RoommateEntity.getAll(userDao)).thenThrow(new BackendException(Response.Status.INTERNAL_SERVER_ERROR,"Erreur DB"));

        // When / Then
        Assertions.assertThrows(BackendException.class, () -> roommateResource.getAll());
    }
}