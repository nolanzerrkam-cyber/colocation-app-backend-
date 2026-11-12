package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SHOPPING_ITEM")
public class ShoppingItemEntity {
    @Id
    @Column(name = "id_shopping", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_shopping;

    @Column(name = "label")
    private String label;

    @ManyToOne
    @JoinColumn(name = "ROOMMATE_EMAIL")
    private RoommateEntity roommate;


    public int getId_shopping() {
        return id_shopping;
    }

    public void setId_shopping(int id_shopping) throws BackendException {
        if (id_shopping < 0) {
            throw new BackendException(Response.Status.BAD_REQUEST, "id cannot be negative");
        }
        this.id_shopping = id_shopping;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) throws BackendException {
        if (label == null || label.isEmpty()) {
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "Label cannot be empty");
        }
        this.label = label;
    }

    public RoommateEntity getRoommate() {
        return roommate;
    }

    public void setRoommate(RoommateEntity roommate) throws BackendException {
        if (roommate == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Roommate is null");

        if (!roommate.equals(this.roommate)) {
            this.roommate = roommate;
        }
    }

    //Constructeurs

    public ShoppingItemEntity() {
    }

    public ShoppingItemEntity(String label, RoommateEntity roommate) throws BackendException {
        setLabel(label);
        setRoommate(roommate);
    }

    public ShoppingItemEntity(int id_shopping, String label, RoommateEntity roommate) throws BackendException {
        this(label, roommate);
        setId_shopping(id_shopping);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShoppingItemEntity)) return false;
        ShoppingItemEntity that = (ShoppingItemEntity) o;
        return id_shopping == that.id_shopping && Objects.equals(label, that.label) && Objects.equals(roommate, that.roommate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_shopping, label, roommate);
    }

    @Override
    public String toString() {
        return "ShoppingItemEntity{" +
                "id_shopping=" + id_shopping +
                ", label='" + label + '\'' +
                ", roommate=" + roommate +
                '}';
    }
}