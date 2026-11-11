package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Entity(name = "Landlord")
public class LandlordEntity extends UserEntity
{
    // I had to modify this entity. There was nothing in it !
    // No flatshare means that the landlord is not linked to the flatshare...
    // For now, this is a prototype, but we have to look at this...
    @OneToMany
    private List<FlatshareEntity> flatshares;

    public LandlordEntity(String email, String password, String firstname, String lastname, String phonenumber, List<FlatshareEntity> flatshares) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
        setFlatshare(flatshares);
    }

    public LandlordEntity()
    {

    }

    public List<FlatshareEntity> getFlatshare()
    {
        return flatshares;
    }

    public void setFlatshare(List<FlatshareEntity> flatshare) throws BackendException {
        if (flatshare == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Flatshare cannot be null");
        // Forced to do this because the variable flatshare is still null when passing
        // though this condition.
        if(!flatshare.equals(this.getFlatshare()))
        {
            this.flatshares = flatshare;
            // ajouter landlord
        }
    }
}
