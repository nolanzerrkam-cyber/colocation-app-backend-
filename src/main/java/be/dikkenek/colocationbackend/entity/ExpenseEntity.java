package be.dikkenek.colocationbackend.entity;
import be.dikkenek.colocationbackend.dao.Dao;
import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;
import java.time.*;
import java.util.Optional;

@Entity
@Table(name ="EXPENSE")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expense")
    private int id;

    @Column(name = "libele", nullable = false, length = 100)
    private String label;

    @Column(name = "price", nullable = false, columnDefinition = "NUMBER(10,2)")
    private float price;

    @Column(name = "date_expense", nullable = false)
    private Date date_expense;

    @ManyToOne
    // gère la relation en BD
    @JoinColumn(name = "Email", nullable = false)
    private RoommateEntity roomate;

    public ExpenseEntity() {
    }

    public ExpenseEntity(String label, float price, RoommateEntity roomate) {
            setLabel(label);
            setPrice(price);
            setDate();
            setRoomate(roomate);
        }

    public int getId() {
        return id;
    }

    private void setId(int id)
    {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    private void setLabel(String label) {
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("label cannot be null or empty");
        }
        this.label = label;
    }

    public float getPrice() {
        return price;
    }

    private void setPrice(float price) {
        if (price <= 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        this.price = price;
    }

    public Date getDate() {
        return date_expense;
    }

    private void setDate() {this.date_expense = new Date();}

    public RoommateEntity getRoomate() {
        return roomate;
    }

    private void setRoomate(RoommateEntity roomate) {
        this.roomate = roomate;
    }

    public static void checkEmail(String email) throws BackendException {
        if (email == null || email.trim().isEmpty())
            throw new BackendException(Response.Status.BAD_REQUEST, "The email is not valid !");
    }

    public static List<ExpenseEntity> findAll(ExpenseDao expenseDao) throws BackendException
    //fonction qui va appeler le DAO pour recuperer toute les depenses
    {
        return expenseDao.getAll();
    }

    public static ExpenseEntity get(ExpenseDao dao, int id) throws BackendException{
        return dao.get(id).orElse(null);
    }
    public boolean create(Dao<ExpenseEntity, Integer> dao) throws BackendException
    {
        return dao.create(this);
    }
    public boolean delete(ExpenseDao dao) throws BackendException
    {
        return dao.delete(this);
    }
}