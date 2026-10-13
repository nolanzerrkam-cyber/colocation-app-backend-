package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.entity.TaskEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class TaskDaoImpl implements Dao<TaskEntity, Integer> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean create(TaskEntity taskEntity) {
        EntityTransaction entityTransaction = em.getTransaction();
        try {
            entityTransaction.begin();
            em.persist(taskEntity);
            entityTransaction.commit();
            return true;
        } catch (Exception exception) {
            entityTransaction.rollback();
            return false;
        }
    }
}
