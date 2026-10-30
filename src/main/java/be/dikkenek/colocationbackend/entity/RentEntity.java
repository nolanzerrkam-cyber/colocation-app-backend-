package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.RentDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Entity
@Table(name ="RENT")
public class RentEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="IdRent")
    private int id;

    @Column(name ="TotalPrice", nullable = false)
    private float totalPrice;

    @Column(name = "deadline", nullable = false)
    private Date deadline;

    @OneToOne
    @JoinColumn(name = "email", nullable = false, unique = true)
    private LandlordEntity landlord;

    //is like a dictionnary<roomate, booleen> but in simple for the ORM
    @OneToMany(mappedBy = "rent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentRoommateStatusEntity> roommatePaymentStatus;
    /*--------------------------------constructor--------------------------------------------*/
    public RentEntity()
    {
        setRoommatePaymentStatus(new ArrayList<RentRoommateStatusEntity>());
    }

    // to use if the landlord want to set the rent but have no roommate yet
    public RentEntity(int id, float price, Date deadline, LandlordEntity landlord) throws BackendException
    {
        setId(id);
        setPrice(price);
        setLandlord(landlord);
        setDeadline(deadline);
        setRoommatePaymentStatus(new ArrayList<RentRoommateStatusEntity>());
    }

    //to use if the landlord want to set the rent but has already a roommate to add
    public RentEntity(int id, float price, Date deadline, LandlordEntity landlord, RoommateEntity roommate) throws BackendException
    {
        setId(id);
        setPrice(price);
        setLandlord(landlord);
        setDeadline(deadline);
        setRoommatePaymentStatus(new ArrayList<RentRoommateStatusEntity>());
        this.addRoomate(roommate);
    }
    /*-------setter and getter---------------------------------------------------*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return totalPrice;
    }

    public void setPrice(float price) throws BackendException {
        if(price <= 0)
            throw new BackendException(Response.Status.BAD_REQUEST, "The price has to be greater than 0 at least, please try again.");
        if(Float.isNaN(price))
            throw new BackendException(Response.Status.BAD_REQUEST, "The price has to be a number, please try again.");
        this.totalPrice = price;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        //TODO make sure the deadline is for at least two week later and a month max
        this.deadline = deadline;
    }

    public List<RentRoommateStatusEntity> getRoommatePaymentStatus() {
        return roommatePaymentStatus;
    }

    public void setRoommatePaymentStatus(List<RentRoommateStatusEntity> roommatePaymentStatus) {
        this.roommatePaymentStatus = roommatePaymentStatus;
    }

    public LandlordEntity getLandlord() {
        return landlord;
    }

    public void setLandlord(LandlordEntity landlord) throws BackendException {
        if(landlord == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "The rent has to be linked to a landlord. Please try again.");
        this.landlord = landlord;
    }
/*----------------function and method--------------------------------------*/
    //TODO function to reset the boolean

    public void removeRoommate(RoommateEntity roommate) {
        roommatePaymentStatus.removeIf(r -> r.getRoommate().equals(roommate));
    }

    public void addRoomate(RoommateEntity roommate)
    {
        //TODO pass by the constructor
        RentRoommateStatusEntity status = new RentRoommateStatusEntity();
        status.setRent(this);
        status.setRoommate(roommate);
        status.setPaid(false);

        if(!roommatePaymentStatus.contains(status))
        {
            this.roommatePaymentStatus.add(status);
        }

    }




//    public boolean create(RentDao dao) throws BackendException
//    {
//        // To check if an email has aleardy been linked to a rent
//        boolean checkForRent = dao.getAll().stream().anyMatch(r -> r.getLandlord().getEmail().equals(landlord.getEmail()));
//        if(checkForRent)
//            throw new BackendException(Response.Status.CONFLICT, "This landlord has already a rent !");
//
//        return dao.create(this);
//    }

    public static RentEntity get(RentDao dao, int id) throws BackendException
    {
        return dao.get(id).orElse(null);
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
