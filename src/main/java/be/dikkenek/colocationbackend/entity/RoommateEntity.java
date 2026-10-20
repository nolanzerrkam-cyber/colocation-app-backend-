package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.RoommateDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.Entity;

import java.util.List;

@Entity(name = "Roommate")
public class RoommateEntity extends UserEntity
{
    public RoommateEntity() {
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
    }

    //Méthodes

    public static List<RoommateEntity> getAll(RoommateDao dao) throws BackendException
    {
        return dao.getAll();
    }
}
