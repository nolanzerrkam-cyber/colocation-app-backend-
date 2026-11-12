package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.core.Response;

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
    public boolean delete(UserEntity user) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            EntityTransaction tr = man.getTransaction();
            try {
                tr.begin();

                if (user == null || user.getEmail() == null) {
                    tr.rollback();
                    return false;
                }

                // tell to hybernate the entity is managed
                UserEntity managedUser = man.merge(user);

                if (managedUser == null) {
                    tr.rollback();
                    return false;
                }
                // delete the entity from the db
                man.remove(managedUser);

                tr.commit();
                return true;

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
}
