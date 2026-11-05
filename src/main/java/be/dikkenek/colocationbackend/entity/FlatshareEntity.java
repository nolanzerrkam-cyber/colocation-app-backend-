package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "FLATSHARE")
/**
 * Classe colocation
 */
public class FlatshareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RoommateEntity> roommates;
    @ManyToOne
    private LandlordEntity landlord;
    @OneToOne
    private RentEntity rent;

    /**
     * VERY IMPORTANT COMMENT
     * We wanted to implement a Singleton design pattern to ensure the FlatshareEntity
     * exists only once but Hibernate needs a public or protected constructor without any parameters
     */
    public FlatshareEntity() {
        setRoommates(new ArrayList<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addRoommate(RoommateEntity roommateEntity) throws BackendException {
        if (roommateEntity == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "RoommateEntity cannot be null");

        roommateEntity.setFlatshare(this);
        if (!roommates.contains(roommateEntity)) roommates.add(roommateEntity);
    }

    public void removeRoommate(RoommateEntity roommateEntity) {
        roommates.remove(roommateEntity);
    }

    public LandlordEntity getLandlord() {
        return landlord;
    }

    public RentEntity getRent() {
        return rent;
    }

    public void setRent(RentEntity rent) throws BackendException {
        if (rent == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Rent is null");

        rent.setFlatshare(this);
        this.rent = rent;
    }

    public void setRoommates(List<RoommateEntity> roommates) {
        this.roommates = roommates;
    }

    public List<RoommateEntity> getRoommates() {
        return roommates;
    }

    public List<ExpenseEntity> getAllExpenses() {
        return roommates.stream().flatMap(r -> r.getExpenses().stream()).collect(Collectors.toList());
    }

    public List<TaskEntity> getAllTasks() {
        return roommates.stream().flatMap(r -> r.getTasks().stream()).collect(Collectors.toList());
    }

    public List<ShoppingItemEntity> getAllShoppingItems() {
        return roommates.stream().flatMap(r -> r.getShoppingItems().stream()).collect(Collectors.toList());
    }

    /**
     *
     * @return the total amount of every expenses of the flatshare
     */
    public float getTotalExpenses() {
        return roommates.stream().map(RoommateEntity::getTotalPrice).reduce(0.0f, Float::sum);
    }

    /**
     *
     * @return true if each roommate paid the rent for this month
     */
    public boolean isCurrentRentPaid() {
        return roommates.stream().allMatch(RoommateEntity::isCurrentRentPaid);
    }

    /**
     *
     * @param month the target month number (1 -> 12)
     * @param year  the target year number
     * @return true if each roommate paid the rent for the specified month and year
     */
    public boolean isRentPaid(int month, int year) {
        return roommates.stream().allMatch(r -> r.isRentPaid(month, year));
    }

    public static FlatshareEntity get(FlatshareDao dao) throws BackendException {
        return dao.getFirst().orElse(null);
    }

    public boolean update(FlatshareDao dao) throws BackendException {
        return dao.update(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FlatshareEntity)) return false;
        FlatshareEntity that = (FlatshareEntity) o;
        return id == that.id && Objects.equals(roommates, that.roommates) && Objects.equals(landlord, that.landlord) && Objects.equals(rent, that.rent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roommates, landlord, rent);
    }

    @Override
    public String toString() {
        return "FlatshareEntity{" +
                "id=" + id +
                ", roommates=" + roommates +
                ", landlord=" + landlord +
                ", rent=" + rent +
                '}';
    }
}