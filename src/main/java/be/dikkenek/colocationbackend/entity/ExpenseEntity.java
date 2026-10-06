package be.dikkenek.colocationbackend.entity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name ="EXPENSE")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_expense")
    private int id;

    @Column(name = "libele", nullable = false, length = 100)
    private String libele;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "date_expense", nullable = false)
    private LocalDate date_expense;

    @ManyToOne
    // gère la relation en BD
    @JoinColumn(name = "Email", nullable = false)
    private ColocEntity coloc;

    public ExpenseEntity() {
    }

    public ExpenseEntity(String libele, float price, ColocEntity coloc) {
        setLibele(libele);
        setPrice(price);
        setDate();
        setColoc(coloc);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    private void setLibele(String libele) {
        if (libele == null || libele.isEmpty()) {
            throw new IllegalArgumentException("libele cannot be null or empty");
        }
        this.libele = libele;
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

    public LocalDate getDate() {
        return date_expense;
    }

    private void setDate() {
        this.date_expense = LocalDate.now();
    }

    public ColocEntity getColoc() {
        return coloc;
    }

    private void setColoc(ColocEntity coloc) {
        this.coloc = coloc;
    }
}