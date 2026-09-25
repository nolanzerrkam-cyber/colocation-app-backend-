package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<UserEntity, String>
{
    private final EntityManager  man;

    public UserDao(EntityManager man)
    {
        this.man = man;
    }

    @Override
    public Optional<UserEntity> get(String id) {
        return Optional.ofNullable(man.find(UserEntity.class, id));
    }

    @Override
    public boolean create(UserEntity entity) {
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

    @Override
    public boolean update(UserEntity entity) {
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

    @Override
    public boolean delete(String id) {
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

    @Override
    public List<UserEntity> getAll()
    {
        return man.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

}
