package be.dikkenek.colocationbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class ExpenseInsertDTO
{
    private BigDecimal price;
    private String libele;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateExpense;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    public LocalDateTime getDateExpense() {
        return dateExpense;
    }

    public void setDateExpense(LocalDateTime dateExpense) {
        this.dateExpense = dateExpense;
    }

    public ExpenseInsertDTO() {
    }
}
