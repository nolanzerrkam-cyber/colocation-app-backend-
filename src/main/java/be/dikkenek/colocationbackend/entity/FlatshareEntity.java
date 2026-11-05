package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
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
    private double rent;

    public FlatshareEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ShoppingItemEntity> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(List<ShoppingItemEntity> shoppingItems) {
        this.shoppingItems = shoppingItems;
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

    public void addItem(ShoppingItemEntity shoppingItemEntity) throws BackendException {
        if (shoppingItemEntity == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "ShoppingItemEntity cannot be null");

        shoppingItemEntity.setFlatshare(this);
        if (!shoppingItems.contains(shoppingItemEntity)) shoppingItems.add(shoppingItemEntity);
    }

    public void removeItem(ShoppingItemEntity shoppingItemEntity) {
        shoppingItems.remove(shoppingItemEntity);
    }

    public LandlordEntity getLandlord() {
        return landlord;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) throws BackendException {
        if (rent <= 0)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Rent must be greater than 0");

        this.rent = rent;
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
}