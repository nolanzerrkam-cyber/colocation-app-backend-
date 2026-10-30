package be.dikkenek.colocationbackend.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "rent_roommate_status")
/*is an association class, who represent the status of payement by a roomate
* it tel if a roomate has paid is part of the rent or not*/
public class RentRoommateStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "IdRent")
    private RentEntity rent;

    // to see if it's correct
    @OneToOne
    @JoinColumn(name = "email")
    private RoommateEntity roommate;

    @Column(name = "isPaid", nullable = false)
    private boolean isPaid;

    public RentRoommateStatusEntity(){}

    public RentRoommateStatusEntity(int id, RentEntity rent, RoommateEntity roommate, boolean isPaid) {
        setId(id);
        setRent(rent);
        setRoommate(roommate);
        setPaid(isPaid);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RentEntity getRent() {
        return rent;
    }

    public void setRent(RentEntity rent) {
        this.rent = rent;
    }

    public RoommateEntity getRoommate() {
        return roommate;
    }

    public void setRoommate(RoommateEntity roommate) {
        this.roommate = roommate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RentRoommateStatusEntity that = (RentRoommateStatusEntity) o;
        return id == that.id && isPaid == that.isPaid && Objects.equals(rent, that.rent) && Objects.equals(roommate, that.roommate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rent, roommate, isPaid);
    }

    @Override
    public String toString() {
        return "RentRoommateStatusEntity{" +
                "id=" + id +
                ", rent=" + rent.getId() +
                ", roommate=" + roommate.getEmail() +
                ", isPaid=" + isPaid +
                '}';
    }
}
