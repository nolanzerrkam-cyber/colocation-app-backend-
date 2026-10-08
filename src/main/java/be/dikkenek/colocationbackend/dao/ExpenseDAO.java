package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

@ApplicationScoped
public class ExpenseDAO implements Dao<ExpenseEntity, Integer>
{
    @Override
    public boolean create (ExpenseEntity expense)
    {
        try(EntityManager em = DatabaseConfig.createEntityManager())
        {
            EntityTransaction et = em.getTransaction();
            try
            {
                et.begin();
                em.persist(expense);
                et.commit();
                return true;
            }
            catch(Exception e)
            {
                if(et.isActive())
                    et.rollback();
                return false;
            }
        }
    }
}
