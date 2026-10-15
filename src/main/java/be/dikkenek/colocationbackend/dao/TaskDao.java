package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.TaskEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class TaskDao implements Dao<TaskEntity, Integer> {

    @Override
    public boolean create(TaskEntity taskEntity) throws BackendException {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            try {
                entityTransaction.begin();
                entityManager.persist(taskEntity);
                entityTransaction.commit();
                return true;
            } catch (Exception exception) {
                entityTransaction.rollback();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error when creating task");
            }
        } catch (Exception exception) {
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error when creating task");
        }
    }
}
