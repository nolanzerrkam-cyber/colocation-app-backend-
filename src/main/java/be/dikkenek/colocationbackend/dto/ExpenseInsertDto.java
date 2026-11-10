package be.dikkenek.colocationbackend.dto;

import java.util.Date;

public class ExpenseInsertDto
{
    private float price;
    private String label;
    private String email;

    private Date dateExpense;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateExpense() {
        return dateExpense;
    }

    public void setDateExpense(Date dateExpense) {
        this.dateExpense = dateExpense;
    }

    public ExpenseInsertDto() {
    }
}
