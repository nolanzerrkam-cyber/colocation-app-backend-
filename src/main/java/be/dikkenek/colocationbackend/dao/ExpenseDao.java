package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@ApplicationScoped
public class ExpenseDao implements Dao<ExpenseEntity, Integer> {
    
    @Override
    public List<ExpenseEntity> getAll() {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ExpenseEntity> criteriaQuery = criteriaBuilder.createQuery(ExpenseEntity.class);
            Root<ExpenseEntity> root = criteriaQuery.from(ExpenseEntity.class);
            criteriaQuery.select(root);
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }
}
