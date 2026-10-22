package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.TaskEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class TaskDao implements Dao<TaskEntity, Integer> {

    @Override
    public List<TaskEntity> getAll() throws BackendException {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);
            Root<TaskEntity> root = criteriaQuery.from(TaskEntity.class);
            criteriaQuery.select(root);
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error when retrieving tasks");
        }
    }

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
