package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Entity
@Table(name ="RENT")
public class RentEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private int id;

    @Column(name ="price", nullable = false)
    private float price;

    @OneToOne
    @JoinColumn(name = "email", nullable = false, unique = true)
    private LandlordEntity landlord;

    public RentEntity()
    {

    }

    public RentEntity(int id, float price, LandlordEntity landlord) throws BackendException
    {
        setId(id);
        setPrice(price);
        setLandlord(landlord);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) throws BackendException {
        if(price <= 0)
            throw new BackendException(Response.Status.BAD_REQUEST, "The price has to be greater than 0 at least, please try again.");
        if(Float.isNaN(price))
            throw new BackendException(Response.Status.BAD_REQUEST, "The price has to be a number, please try again.");
        this.price = price;
    }

    public LandlordEntity getLandlord() {
        return landlord;
    }

    public void setLandlord(LandlordEntity landlord) throws BackendException {
        if(landlord == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "The rent has to be linked to a landlord. Please try again.");
        this.landlord = landlord;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof RentEntity)
            return o.toString().equals(toString());
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.toString());
    }

    @Override
    public String toString()
    {
        return "The RentEntity : ID => " + getId() + ", with the price to pay : " + getPrice() + ", from the landlord : " + landlord.getFullName() + ".";
    }
}
