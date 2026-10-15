package be.dikkenek.colocationbackend.dto;

import java.util.Date;

public class ExpenseInsertDto
{
    private float price;
    private String libele;
    private String email;
    private Date dateExpense;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
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
