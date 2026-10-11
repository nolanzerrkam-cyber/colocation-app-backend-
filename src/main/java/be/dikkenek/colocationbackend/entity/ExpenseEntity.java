package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.Dao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.time.*;

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

    public void setLibele(String libele)
    {
        if(libele == null || libele.length() > 100 || libele.trim().equals(""))
        {
            throw new IllegalArgumentException("[ERROR] The libeled might be null or above 100 characters.");
        }
        this.libele = libele;
    }

    public BigDecimal getPriceExpense() {
        return priceExpense;
    }

    public void setPriceExpense(BigDecimal priceExpense)
    {
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

    public void setExpenseDate(Date expenseDate)
    {
        if(expenseDate == null)
        {
            throw new IllegalArgumentException("[ERROR] The expenseDate is null ! Please check your date and change it.");
        }
        this.expenseDate = expenseDate;
    }

    public String getEmail() {
        return email;
    }

    // For the pull reviewer, do you think its better like my version, or else
    // the better version is to stock a array for a regex, than I check if the regex can be compared
    // to the email ?

    // Now that I look at it, I don't know if my version is good. Let me know with a review comment.
    public void setEmail(String email)
    {
        // Here im checking if the email is null, is above the limit of characters
        // or if the email contains white spaces.
        if(email == null || email.length() > 255 || email.contains(" "))
        {
            throw new IllegalArgumentException("[ERROR] This email \"" + email + "\" is null or contains white spaces or the email length is above 255 characters.");
        }

        // I"m getting the position of the @ and of the dot.
        // Useful, to see if the email has at least one dot and one at.
        int at = email.indexOf("@");
        int checkMoreOneAt = email.lastIndexOf("@");
        int dot = email.lastIndexOf(".");

        boolean onlyOneAt = (at != -1 && at == checkMoreOneAt); // If the at is equals to the lastIndex of the @ in the email, if not that means that we have no @ or that there are more than one @
        boolean textBeforeAt = at >= 1; // If there are at least one character before the at
        boolean dotAfterAt = dot >= at + 2; // If there is at least one 1 character between the @ and the dot.
        boolean extensionOk = dot <= email.length() - 3; // If there are at least 2 characters between the dot and the @

        // Checking if the format is correct.
        if(!onlyOneAt || !textBeforeAt || !dotAfterAt || !extensionOk || !email.contains("@"))
        {
            throw new IllegalArgumentException("[ERROR] Be careful : this email \"" + email + "\" is not in the correct format : please respect => name@domain.com");
        }
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
