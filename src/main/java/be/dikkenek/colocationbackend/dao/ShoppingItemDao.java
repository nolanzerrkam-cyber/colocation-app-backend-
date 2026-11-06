package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class ShoppingItemDao implements Dao<ShoppingItemEntity, Integer>
{
    @Override
    public List<ShoppingItemEntity> getAll() throws BackendException {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            return entityManager
                    .createQuery("SELECT s FROM ShoppingItemEntity s ", ShoppingItemEntity.class)
                    .getResultList();
        } catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error when getting shopping items");
        }
    }

    @Override
    public boolean create(ShoppingItemEntity entity) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            EntityTransaction tr = man.getTransaction();
            try {
                tr.begin();
                man.persist(entity);
                tr.commit();
                return true;
            } catch (Exception e) {
                if (tr.isActive())
                    tr.rollback();
                e.printStackTrace();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create item");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create item");
        }
    }
}
