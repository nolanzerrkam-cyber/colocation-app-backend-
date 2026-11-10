package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.HelloWorldEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@ApplicationScoped
public class HelloWorldDao implements Dao<HelloWorldEntity, Integer> {

    @Override
    public Optional<HelloWorldEntity> get(Integer id) throws BackendException {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(HelloWorldEntity.class, id));
        } catch (Exception exception) {
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error when getting helloworld");
        }
    }

    @Override
    public boolean create(HelloWorldEntity testEntity) throws BackendException {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            try {
                entityTransaction.begin();
                entityManager.persist(testEntity);
                entityTransaction.commit();
                return true;
            } catch (Exception exception) {
                entityTransaction.rollback();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error when creating helloworld");
            }
        } catch (Exception exception) {
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Database error when creating helloworld");
        }
    }
}