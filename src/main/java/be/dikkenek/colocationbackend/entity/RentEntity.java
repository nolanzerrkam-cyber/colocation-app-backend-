package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.Calendar;


/**
 * Classe loyer
 */
@Entity
@Table(name = "RENT")

public class RentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rent")
    private int id_rent;

    @Column(name = "deadlineDay")
    private int deadlineDay;

    @Column(name = "amount", nullable = false, columnDefinition = "NUMBER(10,2)")
    private float amount;


    public RentEntity() {
    }


    /**
     *
     * @return the month length if the deadline is greater than the month length
     */
    public int getDeadlineDay() {
        Calendar calendar = Calendar.getInstance();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return Math.min(deadlineDay, daysInMonth);
    }

    public void setDeadlineDay(int deadlineDay) throws BackendException {
        if(deadlineDay <= 0 || deadlineDay > 31)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Deadline must be between 1 and 31");

        this.deadlineDay = deadlineDay;
    }

    public void setAmount(float amount) throws BackendException {
        if(amount <= 0)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Rent must be greater than 0");
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public int getId_rent() {
        return id_rent;
    }
}
