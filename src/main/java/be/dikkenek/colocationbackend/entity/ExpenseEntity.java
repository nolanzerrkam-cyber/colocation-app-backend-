package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.Dao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name= "EXPENSE")
public class ExpenseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expense")
    private int idExpense;

    @Column(name = "libele", length = 100) // Same reason as price
    private String libele;

    @Column(name = "price", precision = 10, scale = 2) // I have to do that, otherwise hibernate changes the length directly
    private BigDecimal priceExpense;
    // BigDecimal is made to represent decimals (which can be the case for the prices of the expenses)
    // BigDeciaml does not have the float problem (0,1 + 0,2 = 0,3000000001)

    @Column(name = "date_expense")
    private Date expenseDate;

    @Column(name = "email")
    private String email;

    public int getIdExpense() {
        return idExpense;
    }

    public void setIdExpense(int idExpense) {
        this.idExpense = idExpense;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public BigDecimal getPriceExpense() {
        return priceExpense;
    }

    public void setPriceExpense(BigDecimal priceExpense) {
        /* CompareTo will check the value of a (which is priceExpense) compared to b (which is 0)
            I use compareTo for checking if the price is positive, because since BigDecimal is not
            a primitive/primary type, it's an object BigDecimal.
         */
        if(priceExpense == null || priceExpense.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("[ERROR] The expense should have a positive price.");
        }
        this.priceExpense = priceExpense;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ExpenseEntity()
    {

    }

    public boolean create(Dao<ExpenseEntity, Integer> dao)
    {
        return dao.create(this);
    }

    public ExpenseEntity(String libele, BigDecimal priceExpense, Date expenseDate, String email)
    {
        setLibele(libele);
        setPriceExpense(priceExpense);
        setExpenseDate(expenseDate);
        setEmail(email);
    }
}
