package be.dikkenek.colocationbackend.entity;
import be.dikkenek.colocationbackend.dao.Dao;
import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.math.BigDecimal;
import java.time.*;
import java.util.Objects;
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

    public ExpenseEntity(String label, float price, RoommateEntity roomate) throws BackendException{
        setLabel(label);
        setPrice(price);
        setDate();
        setRoomate(roomate);
        if (roomate != null && !roomate.getExpenses().contains(this)) {
            roomate.getExpenses().add(this);
        }
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

public String getLabel() {
        return label;
    }

    private void setLabel(String label) throws BackendException{
        if (label == null || label.isEmpty()) {
            throw new BackendException(Response.Status.BAD_REQUEST, "label cannot be null or empty");
        }
        this.label = label;
    }

    public float getPrice() {
        return price;
    }

    private void setPrice(float price) throws BackendException {
        if (price <= 0) {
            throw new BackendException(Response.Status.BAD_REQUEST, "price cannot be negative");
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

    public void setRoomate(RoommateEntity roomate) {
        this.roomate = roomate;
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExpenseEntity)
            return o.toString().equals(toString());
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.toString());
    }

    @Override
    public String toString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("Expense [id=%d, label=%s, price=%f, date=%s, %s]",getId(),getLabel(),getPrice(),formatter.format(date_expense),getRoomate().toString());
    }
}