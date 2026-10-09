package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.config.DatabaseConfig;
import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.ApplicationPath;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao implements Dao<UserEntity, String>
{
    @Override
    public Optional<UserEntity> get(String id)
    {
        try(EntityManager man = DatabaseConfig.createEntityManager()){
            return Optional.ofNullable(man.find(UserEntity.class, id));
        }

    }

    @Override
    public boolean create(UserEntity entity) {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
            EntityTransaction tr = man.getTransaction();
            try
            {
                tr.begin();
                man.persist(entity);
                tr.commit();
                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                if (tr.isActive())
                {
                    tr.rollback();
                }
                return false;
            }
        }

    }

    @Override
    public boolean update(UserEntity entity) {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
            EntityTransaction tr = man.getTransaction();
            try {
                tr.begin();
                man.merge(entity);
                tr.commit();
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (tr.isActive()) {
                    tr.rollback();
                }
                return false;
            }
        }

    }

    @Override
    public boolean delete(String id) {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
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
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (tr.isActive())
                {
                    tr.rollback();
                }
                return false;
            }
        }
    }

    @Override
    public List<UserEntity> getAll()
    {
        try(EntityManager man = DatabaseConfig.createEntityManager())
        {
            return man.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
        }

    }

}
