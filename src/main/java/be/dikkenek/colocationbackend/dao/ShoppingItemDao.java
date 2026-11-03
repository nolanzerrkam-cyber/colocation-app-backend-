package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
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
}
