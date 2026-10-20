package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class RoommateDao implements Dao<RoommateEntity, String>
{
    @Override
    public List<RoommateEntity> getAll() throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            return man.createQuery("SELECT u FROM Roommate u", RoommateEntity.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to retrieve users list");
        }
    }
}
