package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@ApplicationScoped
public class FlatshareDao implements Dao<FlatshareEntity, Integer> {

    @Override
    public Optional<FlatshareEntity> get(Integer id) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            return Optional.ofNullable(man.find(FlatshareEntity.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error: cannot retrieve Flatshare");
        }
    }

    @Override
    public boolean update(FlatshareEntity entity) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            EntityTransaction tr = man.getTransaction();
            try {
                tr.begin();
                man.merge(entity);
                tr.commit();
                return true;
            } catch (Exception e) {
                if (tr.isActive())
                    tr.rollback();
                e.printStackTrace();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error: cannot update Flatshare");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error: cannot update Flatshare");
        }
    }
}
