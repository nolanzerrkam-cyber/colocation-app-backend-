package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.RoommateResponseDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoommateResourceTest
{
    @Mock
    private UserDao userDao;
    @Mock
    private FlatshareDao flatshareDao;
    @InjectMocks
    private RoommateResource roommateResource;

    @Test
    void roommate_getAll_valid() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommate1 = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);
        RoommateEntity roommate2 = new RoommateEntity("email2@email.com", "password", "b", "b", "0472727272", flatshareEntity);
        List<RoommateEntity> roommates = Arrays.asList(roommate1, roommate2);
        flatshareEntity.setRoommates(roommates);
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

        // When
        Response response = roommateResource.getAll();
        List<RoommateResponseDto> roommateResponseDtos = (List<RoommateResponseDto>) response.getEntity();

        // Then
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(2, roommateResponseDtos.size());
    }

    @Test
    void roommate_getAll_empty() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        Mockito.when(flatshareDao.getFirst()).thenReturn(Optional.of(flatshareEntity));

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
        Mockito.when(flatshareDao.getFirst()).thenThrow(new BackendException(Response.Status.INTERNAL_SERVER_ERROR,"Erreur DB"));

        // When / Then
        Assertions.assertThrows(BackendException.class, () -> roommateResource.getAll());
    }
}