package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "Roommate")
public class RoommateEntity extends UserEntity
{
    public RoommateEntity() {
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
    }

    //Méthodes

    public static List<RoommateEntity> getAll(UserDao dao) throws BackendException
    {
        List<UserEntity> entities = dao.getAll();

        return entities.stream()
                .filter(RoommateEntity.class::isInstance)
                .map(RoommateEntity.class::cast)
                .collect(Collectors.toList());
    }
}
