package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LandlordEntity)) return false;
        if (!super.equals(o)) return false;
        LandlordEntity that = (LandlordEntity) o;
        return Objects.equals(flatshare, that.flatshare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatshare);
    }

    @Override
    public String toString() {
        return "LandlordEntity{" +
                "flatshare=" + flatshare +
                "} " + super.toString();
    }
}
