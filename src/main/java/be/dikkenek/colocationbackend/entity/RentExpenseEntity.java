package be.dikkenek.colocationbackend.entity;

import jakarta.persistence.Entity;

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
}
