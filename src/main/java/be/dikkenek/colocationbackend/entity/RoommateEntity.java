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

    // FetchType.EAGER will load tasks list as soon as RoommateEntity is created
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TaskEntity> tasks;

    @OneToMany(mappedBy = "roomate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ExpenseEntity> expenses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShoppingItemEntity> shoppingItems;

    @ManyToOne
    private FlatshareEntity flatshare;

    public RoommateEntity() {
        setExpenses(new ArrayList<>());
        setTasks(new ArrayList<>());
        setShoppingItems(new ArrayList<>());
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber, FlatshareEntity flatshareEntity) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
        setFlatshare(flatshareEntity);
        setTasks(new ArrayList<>());
        setExpenses(new ArrayList<>());
        setShoppingItems(new ArrayList<>());
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber, FlatshareEntity flatshareEntity, List<TaskEntity> tasks) throws BackendException {
        this(email, password, firstname, lastname, phonenumber, flatshareEntity);
        setTasks(tasks);
    }

    public List<ExpenseEntity> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseEntity> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(ExpenseEntity expense) throws BackendException {
        //methode qui ajoute une depense dans la liste
        if (expense == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "ExpenseEntity cannot be null");

        expense.setRoommate(this);
        if (!expenses.contains(expense)) {
            expenses.add(expense);
        }
    }

    public void removeExpense(ExpenseEntity expense) {
        // methode qui supprime une depense de la liste
        expenses.remove(expense);
    }

    public void addItem(ShoppingItemEntity shoppingItemEntity) throws BackendException {
        if (shoppingItemEntity == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "ShoppingItemEntity cannot be null");

        shoppingItemEntity.setRoommate(this);
        if (!shoppingItems.contains(shoppingItemEntity)) shoppingItems.add(shoppingItemEntity);
    }

    public void removeItem(ShoppingItemEntity shoppingItemEntity) {
        shoppingItems.remove(shoppingItemEntity);
    }

    /**
     *
     * @return the total amount of his/her expenses
     */
    public float getTotalPrice() {
        return expenses.stream().map(ExpenseEntity::getPrice).reduce(0.0f, Float::sum);
    }

    /**
     *
     * @param month the target month (1 -> 12) when the roommate is paying for
     * @param year the target year when the roommate is paying for
     * @throws BackendException from RentExpenseEntity constructor
     */
    public void payRent(int month, int year) throws BackendException {
        String label = "Rent " + month + "/" + year;
        RentExpenseEntity rentExpenseEntity = new RentExpenseEntity(label, getRent(), this, month, year);
        addExpense(rentExpenseEntity);
    }

    /**
     *
     * @return how much the roommate must pay
     */
    public float getRent() {
        return flatshare.getRent().getAmount() / flatshare.getRoommates().size();
    }

    public static List<RoommateEntity> findAll(UserDao userDao) throws BackendException {
        return userDao.getAll().stream()
                .filter(user -> user instanceof RoommateEntity)
                .map(RoommateEntity.class::cast).collect(Collectors.toList());
    }

    public void addTask(TaskEntity taskEntity) throws BackendException {
        if (taskEntity == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "TaskEntity cannot be null");

        taskEntity.setRoommate(this);
        if (!tasks.contains(taskEntity)) this.tasks.add(taskEntity);
    }

    public void removeTask(TaskEntity taskEntity) {
        tasks.remove(taskEntity);
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public List<ShoppingItemEntity> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(List<ShoppingItemEntity> shoppingItems) {
        this.shoppingItems = shoppingItems;
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

    public boolean isCurrentRentPaid() {
        int thisYear = LocalDateTime.now().getYear();
        int thisMonth = LocalDateTime.now().getMonth().getValue();
        return isRentPaid(thisMonth, thisYear);
    }

    public boolean isRentPaid(int month, int year) {
        Optional<RentExpenseEntity> rentExpense = expenses.stream()
                .filter(e -> e instanceof RentExpenseEntity)
                .map(e -> (RentExpenseEntity) e)
                .filter(r -> r.getMonth() == month && r.getYear() == year).findFirst();
        return rentExpense.isPresent();
    }

    public static RoommateEntity get(UserDao dao, String email) throws BackendException {
        return (RoommateEntity) dao.get(email).orElse(null);
    }

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
        return Objects.equals(tasks, that.tasks) && Objects.equals(expenses, that.expenses) && Objects.equals(shoppingItems, that.shoppingItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, expenses, shoppingItems);
    }

    @Override
    public String toString() {
        return "RoommateEntity{" +
                "tasks=" + tasks +
                ", expenses=" + expenses +
                ", shoppingItems=" + shoppingItems +
                '}';
    }
}
