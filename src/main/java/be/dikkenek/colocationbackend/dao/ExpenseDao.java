package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ExpenseDao implements Dao<ExpenseEntity, Integer> {

    @Override
    public List<ExpenseEntity> getAll() throws BackendException {
        try (EntityManager entityManager = DatabaseConfig.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ExpenseEntity> criteriaQuery = criteriaBuilder.createQuery(ExpenseEntity.class);
            Root<ExpenseEntity> root = criteriaQuery.from(ExpenseEntity.class);
            criteriaQuery.select(root);
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to retrieve all Expense");
        }
    }

    @Override
    public Optional<ExpenseEntity> get(Integer id) throws BackendException {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            return Optional.ofNullable(man.find(ExpenseEntity.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to retrieve Expense");
        }
    }
    @Override
    public boolean delete(ExpenseEntity expense) throws BackendException
    {
        try (EntityManager man = DatabaseConfig.createEntityManager()) {
            EntityTransaction tr = man.getTransaction();
            try {
                tr.begin();

                if (expense == null) {
                    tr.rollback();
                    return false;
                }

                // tell to hybernate the entity is managed
                ExpenseEntity managedExpense = man.merge(expense);

                if (managedExpense == null) {
                    tr.rollback();
                    return false;
                }
                // delete the entity from the db
                man.remove(managedExpense);

                tr.commit();
                return true;

            } catch (Exception e) {
                if (tr.isActive())
                    tr.rollback();
                e.printStackTrace();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete expense");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete expense ");
        }
    }

    @Override
    public boolean create(ExpenseEntity expense) throws BackendException {
        try (EntityManager em = DatabaseConfig.createEntityManager()) {
            EntityTransaction et = em.getTransaction();
            try {
                et.begin();
                em.persist(expense);
                et.commit();
                return true;
            } catch (Exception e) {
                if (et.isActive())
                    et.rollback();
                e.printStackTrace();
                throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create expense");
            }
        } catch (BackendException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create expense");
        }
    }
}
