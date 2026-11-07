package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.Response;

/*
classe roomate (colocataire) qui permet de créer un colocataire :
attribut : - email, password, firstname, lastname, phonenumber (heriter de la classe user)
           - expenses (liste de depense)
*/
@Entity(name = "Roommate")
public class RoommateEntity extends UserEntity {

    @ManyToOne
    private FlatshareEntity flatshare;

    public RoommateEntity() {
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber, FlatshareEntity flatshareEntity) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
        setFlatshare(flatshareEntity);
    }

    public FlatshareEntity getFlatshare() {
        return flatshare;
    }

    public void setFlatshare(FlatshareEntity flatshare) throws BackendException {
        if (flatshare == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Flatshare cannot be null");

        if (!flatshare.equals(this.flatshare)) {
            this.flatshare = flatshare;
            flatshare.addRoommate(this);
        }
    }

    /**
     *
     * @return the total amount of the roommate's expenses
     */
    public float getTotalExpenses() {
        return flatshare.getExpenses().stream().map(ExpenseEntity::getPrice).reduce(0.0f, Float::sum);
    }

    /**
     *
     * @param month the target month (1 -> 12) when the roommate is paying for
     * @param year  the target year when the roommate is paying for
     * @throws BackendException from RentExpenseEntity constructor
     */
    public void payRent(int month, int year) throws BackendException {
        String label = "Rent " + month + "/" + year;
        RentExpenseEntity rentExpenseEntity = new RentExpenseEntity(label, getRent(), this, month, year);
        flatshare.addExpense(rentExpenseEntity);
    }

    /**
     *
     * @return how much the roommate must pay
     */
    public float getRent() {
        return flatshare.getRent().getAmount() / flatshare.getRoommates().size();
    }

    /**
     *
     * @return true if the rent of the current month is paid
     */
    public boolean isCurrentRentPaid() {
        int thisYear = LocalDateTime.now().getYear();
        int thisMonth = LocalDateTime.now().getMonth().getValue();
        return isRentPaid(thisMonth, thisYear);
    }

    /**
     *
     * @param month targeted month number (1 -> 12)
     * @param year targeted year number
     * @return true if the rent of the targeted month and targeted year is paid
     */
    public boolean isRentPaid(int month, int year) {
        Optional<RentExpenseEntity> rentExpense = flatshare.getExpenses().stream()
                .filter(e -> e instanceof RentExpenseEntity)
                .map(e -> (RentExpenseEntity) e)
                .filter(r -> r.getMonth() == month && r.getYear() == year).findFirst();
        return rentExpense.isPresent();
    }

    /**
     * Retrieve a roommate object from the database based on his email
     * @param dao
     * @param email
     * @return
     * @throws BackendException
     */
    public static RoommateEntity get(UserDao dao, String email) throws BackendException {
        return (RoommateEntity) dao.get(email).orElse(null);
    }

    /**
     * Retrieve all roommates from the database
     * @param dao
     * @return
     * @throws BackendException
     */
    public static List<RoommateEntity> getAll(UserDao dao) throws BackendException {
        List<UserEntity> entities = dao.getAll();

        return entities.stream()
                .filter(RoommateEntity.class::isInstance)
                .map(RoommateEntity.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoommateEntity)) return false;
        RoommateEntity that = (RoommateEntity) o;
        return Objects.equals(flatshare, that.flatshare);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flatshare);
    }

    @Override
    public String toString() {
        return "RoommateEntity{" +
                "flatshare=" + flatshare +
                '}';
    }
}
