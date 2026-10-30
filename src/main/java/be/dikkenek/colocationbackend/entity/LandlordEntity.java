package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity(name = "Landlord")
public class LandlordEntity extends UserEntity
{
    @OneToOne(mappedBy = "landlord", cascade = CascadeType.ALL)
    private RentEntity rent;

    public LandlordEntity()
    {
    }



    public LandlordEntity(String email, String password, String firstname, String lastname, String phonenumber, RentEntity rent) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
        setRent(rent);
    }

    public RentEntity getRent() {
        return rent;
    }

    public void setRent(RentEntity rent) {
        this.rent = rent;
    }
}
