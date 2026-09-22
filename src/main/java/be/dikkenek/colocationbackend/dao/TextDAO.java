package be.dikkenek.colocationbackend.dao;


import be.dikkenek.colocationbackend.entity.Text;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TextDAO
{
    private EntityManager man;

    public TextDAO(EntityManager man)
    {
        this.man = man;
    }

    public boolean Create(Text txt)
    {
        EntityTransaction tr = man.getTransaction();
        try
        {
            tr.begin();
            man.persist(txt);
            tr.commit();
            return true;
        }
        catch(Exception e)
        {
            if (tr.isActive())
            {
                tr.rollback();
            }
            e.printStackTrace();
            return false;
        }
        finally
        {
            man.close();
        }
    }

    public Text findById(Long id)
    {
        return man.find(Text.class, id);
    }
}
