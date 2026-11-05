package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Entity;
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
    public boolean delete(ShoppingItemEntity shopItem) throws BackendException
    {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
            EntityTransaction et = man.getTransaction();
            try
            {
                et.begin();

                if(shopItem == null)
                {
                    et.rollback();
                    return false;
                }

                ShoppingItemEntity managedShoppingItem = man.merge(shopItem);

                if(managedShoppingItem == null)
                {
                    et.rollback();
                    return false;
                }

                man.remove(managedShoppingItem);

                et.commit();
                return true;
            }
            catch(Exception e)
            {
                if(et.isActive())
                    et.rollback();
                e.printStackTrace();

                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete the shopping item.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete the shopping item.");
        }
    }
}
