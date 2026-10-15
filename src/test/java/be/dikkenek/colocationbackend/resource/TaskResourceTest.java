package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.TaskDao;
import be.dikkenek.colocationbackend.dto.TaskCreateDto;
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

@ExtendWith(MockitoExtension.class)
public class TaskResourceTest {
    @Mock
    private TaskDao taskDao;
    @InjectMocks
    private TaskResource taskResource;

    @Test
    void testPostTask_Success() throws BackendException {
        // Given
        TaskCreateDto inputTask = new TaskCreateDto("Task name", "Task description", false, new Date());
        TaskEntity outputEntity = new TaskEntity(inputTask.getName(), inputTask.getDescription(), inputTask.isDone(), inputTask.getDeadline());
        Mockito.when(taskDao.create(Mockito.any(TaskEntity.class))).thenReturn(true);

        // When
        Response response = taskResource.postTask(inputTask);

        // 3. Then
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void testPostTask_NotSuccess() throws BackendException {
        // Given
        TaskCreateDto inputTask = new TaskCreateDto("Task name", "Task description", false, new Date());
        TaskEntity outputEntity = new TaskEntity("Task name", "Task description", false, new Date());
        Mockito.when(taskDao.create(Mockito.any(TaskEntity.class))).thenReturn(false);

        // When
        Response response = taskResource.postTask(inputTask);

        // 3. Then
        Assertions.assertEquals(500, response.getStatus());
    }
}
