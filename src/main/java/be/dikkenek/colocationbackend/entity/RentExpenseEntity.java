package be.dikkenek.colocationbackend.entity;

import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
/**
 * Classe dépense loyer (dérivée de Expense)
 */
public class RentExpenseEntity extends ExpenseEntity {
    private int month;
    private int year;

    public RentExpenseEntity() {
    }

    public int getMonth() {
        return month;
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
                '}';
    }
}
