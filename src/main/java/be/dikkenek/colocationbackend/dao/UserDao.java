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
    public boolean delete(UserEntity user) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            EntityTransaction tr = man.getTransaction();
            try {
                tr.begin();
                String email = user.getEmail();

                // First delete from the child table (Roommate or Landlord)
                man.createQuery("DELETE FROM Roommate r WHERE r.email = :email")
                        .setParameter("email", email)
                        .executeUpdate();

                man.createQuery("DELETE FROM Landlord l WHERE l.email = :email")
                        .setParameter("email", email)
                        .executeUpdate();

                // Delete from the parent entity (uses the entity name, not the database name)
                int deletedCount = man.createQuery("DELETE FROM UserEntity u WHERE u.email = :email")
                        .setParameter("email", email)
                        .executeUpdate();

                if (deletedCount > 0) {
                    tr.commit();
                    return true;
                } else {
                    tr.rollback();
                    return false;
                }

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
