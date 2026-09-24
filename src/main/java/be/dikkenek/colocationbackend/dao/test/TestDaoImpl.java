package be.dikkenek.colocationbackend.dao.test;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.TestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class TestDaoImpl implements TestDao {

    @Override
    public Optional<TestEntity> getTestById(int id) {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(TestEntity.class, id));
        }
    }

    @Override
    public boolean save(TestEntity testEntity) {
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