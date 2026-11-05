package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

@Entity
/**
 * Classe loyer
 */
public class RentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private int deadlineDay;
    private double amount;
    @OneToOne
    private FlatshareEntity flatshare;

    public RentEntity() {
    }

    public void setFlatshare(FlatshareEntity flatshare) throws BackendException {
        if (flatshare == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Flatshare is null");

        flatshare.setRent(this);
        this.flatshare = flatshare;
    }

    /**
     *
     * @return the month length if the deadline is greater than the month length
     */
    public int getDeadlineDay() {
        Month thisMonth = LocalDateTime.now().getMonth();
        boolean isLeap = Year.now().isLeap();
        return Math.min(deadlineDay, thisMonth.length(isLeap));
    }

    public void setDeadlineDay(int deadlineDay) throws BackendException {
        if(deadlineDay <= 0 || deadlineDay > 31)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Deadline must be between 1 and 31");

        this.deadlineDay = deadlineDay;
    }
}
