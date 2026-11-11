package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Entity
@Table(name="rent_expense")
/**
 * Classe dépense loyer (dérivée de Expense)
 */
public class RentExpenseEntity extends ExpenseEntity {
    private int month;
    private int year;

    public RentExpenseEntity() {
    }

    public RentExpenseEntity(String label, float price, RoommateEntity roommate, int month, int year) throws BackendException {
        super(label, price, roommate);
        setMonth(month);
        setYear(year);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) throws BackendException {
        if (month < 1 || month > 12)
            throw new BackendException(Response.Status.BAD_REQUEST, "Month must be between 1 and 12");

        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RentExpenseEntity)) return false;
        if (!super.equals(o)) return false;
        RentExpenseEntity that = (RentExpenseEntity) o;
        return month == that.month && year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), month, year);
    }

    @Override
    public String toString() {
        return "RentExpenseEntity{" +
                "month=" + month +
                ", year=" + year +
                "} " + super.toString();
    }
}
