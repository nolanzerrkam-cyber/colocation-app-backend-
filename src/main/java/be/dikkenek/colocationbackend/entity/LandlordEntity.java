package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.ws.rs.core.Response;

@Entity(name = "Landlord")
public class LandlordEntity extends UserEntity {
    @OneToOne
    private FlatshareEntity flatshare;

    public LandlordEntity(String email, String password, String firstname, String lastname, String phonenumber, FlatshareEntity flatshare) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
        setFlatshare(flatshare);
    }

    public LandlordEntity() {
    }

    public FlatshareEntity getFlatshare() {
        return flatshare;
    }

    public void setFlatshare(FlatshareEntity flatshare) throws BackendException {
        if (flatshare == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Flatshare cannot be null");

        if (!flatshare.equals(this.getFlatshare())) {
            this.flatshare = flatshare;
            flatshare.setLandlord(this);
        }
    }
}
