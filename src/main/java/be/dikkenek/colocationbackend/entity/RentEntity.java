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

    //This is like a dictionary<roommate, boolean> but in simple for the ORM
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

    /*----------------------------setter and getter--------------------------*/

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

    // This is going to get used in the endpoint, to know who paid their rent.
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

/*------------------------function and method-----------------------*/

    /* This method is made for resetting the deadline of the payement.
        When over, it's going to generate a new deadline for the next month.
        For each RentRoommateStatusEntity, we are going to put the isPaid boolean
        Into false to restart a payment.
     */
    public void resetPayement() throws BackendException
    {
        // Check if the deadline of the object is null or if the date of today has aleardy passed the deadline.
        if(deadline == null || !new Date().after(deadline))
        {
            throw new BackendException(Response.Status.BAD_REQUEST, "Please put a correct deadline to continue.");
        }

        // By setting false, that means that roommates has to pay their rent for the next deadline
        for(RentRoommateStatusEntity r : roommatePaymentStatus)
        {
            r.setPaid(false);
        }

        // It's just to set the deadline to the next month.
        // I changed this part because if the date for example is 3rd december and the deadline was 2nd november, the next deadline would be 2nd december (not correct)
        Calendar nextDeadline = Calendar.getInstance();
        nextDeadline.setTime(deadline);
        Date d = new Date();
        do
        {
            nextDeadline.add(Calendar.MONTH, 1);
        }
        while(!nextDeadline.getTime().after(d));
        this.deadline = nextDeadline.getTime();
    }

    // This function is made to set the deadline of the rent.
    // It checks if the deadline is at least at two weeks later as the date from today,
    // and maximum at one month.
    public void setDeadline(Date deadline) throws BackendException
    {
        if(deadline == null)
        {
            throw new BackendException(Response.Status.BAD_REQUEST, "This deadline is null ! Please put a deadline before validing !");
        }

        // To put the business rule, I have to use Calendar.
        // It's not can use Date only, but its way more weird, and you have to
        // put the limits in milliseconds... It's better to use Calendar I believe.
        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, 14);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 1);

        if(deadline.before(min.getTime()) || deadline.after(max.getTime()))
        {
            throw new BackendException(Response.Status.BAD_REQUEST, "This deadline is incorrect, you should put the deadline between two weeks later and at maximum one month later.");
        }
        this.deadline = deadline;
    }

    public float getAmountPerRoommate() throws BackendException
    {
        if(roommatePaymentStatus.isEmpty())
        {
            throw new BackendException(Response.Status.BAD_REQUEST, "The rent needs roommates to divide the money.");
        }

        float amount = totalPrice / roommatePaymentStatus.size();

        // To make the value round to the hundredth, we need to multiply by 100, than to round the value of the decimal, and finally divide it by 100.
        return Math.round(amount * 100) / 100f;
    }

    public void removeRoommate(RoommateEntity roommate) {
        roommatePaymentStatus.removeIf(r -> r.getRoommate().equals(roommate));
    }

    public void addRoomate(RoommateEntity roommate)
    {
        RentRoommateStatusEntity status = new RentRoommateStatusEntity(this, roommate, false);

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

    public boolean update(RentDao rentDao) throws BackendException
    {
        return rentDao.update(this);
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
