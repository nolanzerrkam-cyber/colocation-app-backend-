package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.TaskDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.TaskCreateDto;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.TaskEntity;
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

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskResourceTest {
    @Mock
    private TaskDao taskDao;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private TaskResource taskResource;

    @Test
    void testPostTask_Success() throws BackendException {
        // Given
        TaskCreateDto inputTask = new TaskCreateDto("Task name", "Task description", false, new Date(), "email@email.com");
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com","password","firstname","lastname", "0404040404");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(roommateEntity));
        Mockito.when(userDao.update(Mockito.any(UserEntity.class))).thenReturn(true);

        // When
        Response response = taskResource.postTask(inputTask);

        // 3. Then
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void testPostTask_NotSuccess() throws BackendException {
        // Given
        TaskCreateDto inputTask = new TaskCreateDto("Task name", "Task description", false, new Date(), "email@email.com");
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com","password","firstname","lastname", "0404040404");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(roommateEntity));
        Mockito.when(userDao.update(Mockito.any(UserEntity.class))).thenReturn(false);

        // When
        Response response = taskResource.postTask(inputTask);

        // 3. Then
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    void testPostTask_user_not_found() throws BackendException {
        // Given
        TaskCreateDto inputTask = new TaskCreateDto("Task name", "Task description", false, new Date(), "email@email.com");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.empty());

        // When
        Response response = taskResource.postTask(inputTask);

        // 3. Then
        Assertions.assertEquals(400, response.getStatus());
    }
}
