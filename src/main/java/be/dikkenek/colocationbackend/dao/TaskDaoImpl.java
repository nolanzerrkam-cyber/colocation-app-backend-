package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.TaskEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TaskDaoImpl implements Dao<TaskEntity, Integer> {

    @Override
    public boolean create(TaskEntity taskEntity) {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            try {
                entityTransaction.begin();
                entityManager.persist(taskEntity);
                entityTransaction.commit();
                return true;
            } catch (Exception exception) {
                entityTransaction.rollback();
                return false;
            }
        }
    }
}
