package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.HelloWorldEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

@ApplicationScoped
public class HelloWorldDaoImpl implements Dao<HelloWorldEntity, Integer> {

    @Override
    public Optional<HelloWorldEntity> get(Integer id) {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(HelloWorldEntity.class, id));
        }
    }

    @Override
    public boolean create(HelloWorldEntity testEntity) {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            try {
                entityTransaction.begin();
                entityManager.persist(testEntity);
                entityTransaction.commit();
                return true;
            } catch (Exception exception) {
                entityTransaction.rollback();
                return false;
            }
        }
    }
}