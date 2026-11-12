package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.ShoppingItemEntity;
import be.dikkenek.colocationbackend.entity.ShoppingQuantityEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

public class ShoppingQuantityDao implements Dao<ShoppingQuantityEntity, Integer>{
    public Optional<ShoppingQuantityEntity> get(Integer id) throws BackendException
    {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
            return Optional.ofNullable(man.find(ShoppingQuantityEntity.class, id));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new BackendException(Response.Status.NOT_FOUND, "We failed to retrieve the shopping item.");
        }
    }
}
