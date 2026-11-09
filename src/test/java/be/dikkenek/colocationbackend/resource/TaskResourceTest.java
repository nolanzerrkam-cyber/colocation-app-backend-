package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.TaskDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.TaskRequestDto;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.TaskEntity;
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
    @Mock
    private FlatshareDao flatshareDao;
    @InjectMocks
    private TaskResource taskResource;

    @Test
    void testPostTask_Success() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        TaskRequestDto inputTask = new TaskRequestDto("Task name", "Task description", false, new Date(), "email@email.com");
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com","password","firstname","lastname", "0404040404", flatshareEntity);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(roommateEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(true);

        // When
        Response response = taskResource.postTask(inputTask);

        // Then
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void testPostTask_NotSuccess() throws BackendException {
        // Given
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        TaskRequestDto inputTask = new TaskRequestDto("Task name", "Task description", false, new Date(), "email@email.com");
        RoommateEntity roommateEntity = new RoommateEntity("email@email.com","password","firstname","lastname", "0404040404", flatshareEntity);
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(roommateEntity));
        Mockito.when(flatshareDao.update(Mockito.any(FlatshareEntity.class))).thenReturn(false);

        // When
        Response response = taskResource.postTask(inputTask);

        // Then
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    void testPostTask_user_not_found() throws BackendException {
        // Given
        TaskRequestDto inputTask = new TaskRequestDto("Task name", "Task description", false, new Date(), "email@email.com");
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.empty());

        // When
        Response response = taskResource.postTask(inputTask);

        // Then
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void test_deleteTask_not_found() throws BackendException {
        // Given
        int id = 0;
        Mockito.when(taskDao.get(Mockito.any(Integer.class))).thenReturn(Optional.empty());

        // When
        Response response = taskResource.deleteTask(id);

        // Then
        Assertions.assertEquals(404, response.getStatus());
    }
    @Test
    void testValidateTask_Success() throws BackendException {
        // Given
        int taskId = 1;
        TaskRequestDto inputData = new TaskRequestDto("Updated name", "Updated description", true, new Date(), "email@email.com");
        TaskEntity existingTask = new TaskEntity();
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommateEntity = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);

        Mockito.when(taskDao.get(taskId)).thenReturn(Optional.of(existingTask));
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(roommateEntity));
        Mockito.when(taskDao.update(Mockito.any(TaskEntity.class))).thenReturn(true);

        // When
        Response response = taskResource.validateTask(taskId, inputData);

        // Then
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void testValidateTask_NotFound() throws BackendException {
        // Given
        int taskId = 99;
        TaskRequestDto inputData = new TaskRequestDto("Name", "Description", false, new Date());

        Mockito.when(taskDao.get(taskId)).thenReturn(Optional.empty());

        // When
        Response response = taskResource.validateTask(taskId, inputData);

        // Then
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    void testValidateTask_UpdateError() throws BackendException {
        // Given
        int taskId = 1;
        TaskRequestDto inputData = new TaskRequestDto("Updated name", "Updated description", true, new Date(), "email@email.com");
        TaskEntity existingTask = new TaskEntity();
        FlatshareEntity flatshareEntity = new FlatshareEntity();
        RoommateEntity roommateEntity = new RoommateEntity("email1@email.com", "password", "a", "a", "0474998234", flatshareEntity);

        Mockito.when(taskDao.get(taskId)).thenReturn(Optional.of(existingTask));
        Mockito.when(userDao.get(Mockito.any(String.class))).thenReturn(Optional.of(roommateEntity));
        Mockito.when(taskDao.update(Mockito.any(TaskEntity.class))).thenReturn(false);

        // When
        Response response = taskResource.validateTask(taskId, inputData);

        // Then
        Assertions.assertEquals(500, response.getStatus());
    }
}
