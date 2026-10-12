package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.Entity;

@Entity(name = "Landlord")
public class LandlordEntity extends UserEntity
{

    public LandlordEntity()
    {
    }

    public LandlordEntity(String email, String password, String firstname, String lastname, String phonenumber) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
    }
}
