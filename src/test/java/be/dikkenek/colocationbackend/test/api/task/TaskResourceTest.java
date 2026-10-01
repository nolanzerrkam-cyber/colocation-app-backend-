package be.dikkenek.colocationbackend.test.api.task;

import be.dikkenek.colocationbackend.dao.TaskDaoImpl;
import be.dikkenek.colocationbackend.entity.TaskEntity;
import be.dikkenek.colocationbackend.resource.TaskResource;
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
    private TaskDaoImpl taskDao = new TaskDaoImpl();
    @InjectMocks
    private TaskResource taskResource;

    @Test
    void testPostTask_Success() {
        // Given
        TaskEntity inputTask = new TaskEntity(-1, "Task name", "Task description", false, new Date());
        Mockito.when(taskDao.create(Mockito.any(TaskEntity.class))).thenReturn(true);

        // When
        Response response = taskResource.postTask(inputTask);

        // 3. Then
        Assertions.assertEquals(201, response.getStatus());

        // Check dao create method has been called 1 time only
        Mockito.verify(taskDao, Mockito.times(1)).create(inputTask);
    }

    @Test
    void testPostTask_NotSuccess() {
        // Given
        TaskEntity inputTask = new TaskEntity(-1, "Task name", "Task description", false, new Date());
        Mockito.when(taskDao.create(Mockito.any(TaskEntity.class))).thenReturn(false);

        // When
        Response response = taskResource.postTask(inputTask);

        // 3. Then
        Assertions.assertEquals(403, response.getStatus());

        // Check dao create method has been called 1 time only
        Mockito.verify(taskDao, Mockito.times(1)).create(inputTask);
    }
}
