package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import jakarta.persistence.Entity;

@Entity
public class LandlordEntity extends UserEntity
{

    public LandlordEntity()
    {
    }

    public LandlordEntity(String email, String password, String firstname, String lastname, String phonenumber) {
        super(email, password, firstname, lastname, phonenumber);
    }
}
