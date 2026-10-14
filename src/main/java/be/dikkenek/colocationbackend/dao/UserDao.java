package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao implements Dao<UserEntity, String> {
    @Override
    public Optional<UserEntity> get(String id) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            return Optional.ofNullable(man.find(UserEntity.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to retrieve user");
        }
    }

    @Override
    public boolean create(UserEntity entity) throws BackendException {
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
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create user");
        }
    }

    @Override
    public boolean update(UserEntity entity) throws BackendException {
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
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to update user");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to update user");
        }
    }

    @Override
    public boolean delete(String id) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            EntityTransaction tr = man.getTransaction();
            try {
                tr.begin();
                UserEntity entity = man.find(UserEntity.class, id);
                if (entity != null) {
                    man.remove(entity);
                    tr.commit();
                    return true;
                }
                tr.rollback();
                return false;
            } catch (Exception e) {
                if (tr.isActive())
                    tr.rollback();
                e.printStackTrace();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete user");
        }
    }

    @Override
    public List<UserEntity> getAll() throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            return man.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to retrieve users list");
        }
    }
}
