package be.dikkenek.colocationbackend.dao;


import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.RentEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

public class RentDao implements Dao<RentEntity, Integer>
{
    @Override
    public Optional<RentEntity> get(Integer id) throws BackendException
    {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
            return Optional.ofNullable(man.find(RentEntity.class, id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BackendException(Response.Status.BAD_REQUEST, "We failed to find your rent. Please try again and check if the id is correct.");
        }
    }

    @Override
    public boolean create(RentEntity rent) throws BackendException
    {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
            EntityTransaction et = man.getTransaction();
            try
            {
                et.begin();
                man.persist(rent);
                et.commit();
                return true;
            }
            catch(Exception e)
            {
                if(et.isActive())
                    et.rollback();
                e.printStackTrace();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "We failed to create the rent.");
            }
        }
        catch (BackendException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "We failed to create the rent.");
        }
    }
}
