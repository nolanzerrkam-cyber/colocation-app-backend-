package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.TestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class TestDaoImpl implements Dao<TestEntity, Integer> {
    @Override
    public Optional<TestEntity> get(Integer id) {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(TestEntity.class, id));
        }
    }

    @Override
    public boolean create(TestEntity testEntity) {
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