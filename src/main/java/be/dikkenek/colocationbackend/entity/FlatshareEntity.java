package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "FLATSHARE")
/**
 * Classe colocation
 */
public class FlatshareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_flatshare")
    private int id_flatshare;

    @ManyToOne
    private LandlordEntity landlord;

    @OneToOne(cascade = CascadeType.ALL)
    private RentEntity rent;

    @OneToMany(mappedBy = "flatshare", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RoommateEntity> roommates;

    // FetchType.EAGER will load tasks list as soon as RoommateEntity is created
    @OneToMany(mappedBy = "flatshare", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TaskEntity> tasks;

    @OneToMany(mappedBy = "flatshare", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ExpenseEntity> expenses;

    @OneToMany(mappedBy = "flatshare", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShoppingQuantityEntity> shoppingQuantityItems;

    public int getId_flatshare() {
        return id_flatshare;
    }

    public void setId_flatshare(int id_flatshare) {
        this.id_flatshare = id_flatshare;
    }

    public LandlordEntity getLandlord() {
        return landlord;
    }

    public void setLandlord(LandlordEntity landlord) throws BackendException {
        if (landlord == null) {
            throw new BackendException(Response.Status.BAD_REQUEST, "the landlord can't be null.");
        }
        if (!landlord.equals(this.landlord)) {
            this.landlord = landlord;
            landlord.setFlatshare(this);
        }
    }

    public RentEntity getRent() {
        return rent;
    }

    public void setRent(RentEntity rent) throws BackendException {
        if (rent == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Rent is null");

        if (this.getRent() != rent) {
            this.rent = rent;
        }
    }

    public void setRoommates(List<RoommateEntity> roommates) {
        this.roommates = roommates == null ? new ArrayList<>() : roommates;
    }

    public List<RoommateEntity> getRoommates() {
        return roommates;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks == null ? new ArrayList<>() : tasks;
    }

    public List<ShoppingQuantityEntity> getShoppingQuantityItems() {
        return shoppingQuantityItems;
    }

    public void setShoppingQuantityItems(List<ShoppingQuantityEntity> shoppingQuantityItems) {
        this.shoppingQuantityItems = shoppingQuantityItems == null ? new ArrayList<>() : shoppingQuantityItems;
    }

    public List<ExpenseEntity> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseEntity> expenses) {
        this.expenses = expenses == null ? new ArrayList<>() : expenses;
    }


    //
    //
    //Constructeurs
    //
    //

    /**
     * VERY IMPORTANT COMMENT
     * We wanted to implement a Singleton design pattern to ensure the FlatshareEntity
     * exists only once but Hibernate needs a public or protected constructor without any parameters
     */
    public FlatshareEntity() {
        setRoommates(new ArrayList<>());
        setTasks(new ArrayList<>());
        setExpenses(new ArrayList<>());
        setShoppingQuantityItems(new ArrayList<>());
    }

    //
    //
    //Méthodes
    //
    //

    /**
     *
     * @return the total amount of every expenses of the flatshare
     */
    public float getTotalExpenses() {
        return roommates.stream().map(RoommateEntity::getTotalExpenses).reduce(0.0f, Float::sum);
    }

    public static FlatshareEntity get(FlatshareDao dao) throws BackendException {
        return dao.getFirst().orElse(null);
    }

    public boolean update(FlatshareDao dao) throws BackendException {
        return dao.update(this);
    }

    public void addItem(ShoppingQuantityEntity shoppingQuantityEntity) throws BackendException {
        if (shoppingQuantityEntity == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "ShoppingItemEntity cannot be null");

        if (!shoppingQuantityItems.contains(shoppingQuantityEntity)) shoppingQuantityItems.add(shoppingQuantityEntity);
    }

    public void removeItem(ShoppingQuantityEntity shoppingQuantityEntity) {
        shoppingQuantityItems.remove(shoppingQuantityEntity);
    }

    public void addExpense(ExpenseEntity expense) throws BackendException {
        if (expense == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "ExpenseEntity cannot be null");

        if (!expenses.contains(expense)) expenses.add(expense);
    }

    public void removeExpense(ExpenseEntity expense) {
        expenses.remove(expense);
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

    public void addTask(TaskEntity taskEntity) throws BackendException {
        if (taskEntity == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "TaskEntity cannot be null");

        if (!tasks.contains(taskEntity)) this.tasks.add(taskEntity);
    }

    public void removeTask(TaskEntity taskEntity) {
        tasks.remove(taskEntity);
    }

    public RoommateEntity getRoommate(String email) {
        if (email == null)
            return null;

        return getRoommates().stream()
                .filter(r -> r.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public ExpenseEntity getExpense(int id) {
        return getExpenses().stream().filter(e -> e.getId_expense() == id).findFirst().orElse(null);
    }

    public TaskEntity getTask(int id) {
        return getTasks().stream().filter(t -> t.getId_task() == id).findFirst().orElse(null);
    }

    public ShoppingQuantityEntity getItem(int id) {
        return getShoppingQuantityItems().stream().filter(i -> i.getId_quantity() == id).findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FlatshareEntity)) return false;
        FlatshareEntity that = (FlatshareEntity) o;
        return id_flatshare == that.id_flatshare && Objects.equals(roommates, that.roommates) && Objects.equals(landlord, that.landlord) && Objects.equals(rent, that.rent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_flatshare, roommates, landlord, rent);
    }

    @Override
    public String toString() {
        return "FlatshareEntity{" +
                "id_flatshare=" + id_flatshare +
                ", roommates=" + roommates +
                ", landlord=" + landlord +
                ", rent=" + rent +
                '}';
    }
}