package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.ShoppingItemDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SHOPPING_ITEM")
public class ShoppingItemEntity
{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "label")
    private String label;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne
    private RoommateEntity roommate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) throws BackendException
    {
        if(label == null || label.isEmpty())
        {
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR,"Label cannot be empty");
        }
        this.label = label;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws BackendException {
        if(quantity <= 0)
        {
            throw new BackendException(Response.Status.BAD_REQUEST, "Quantity must be positive");
        }
        this.quantity = quantity;
    }

    public RoommateEntity getRoommate() {
        return roommate;
    }

    public void setRoommate(RoommateEntity roommate) throws BackendException {
        if(roommate == null)
        {
            throw new BackendException(Response.Status.BAD_REQUEST, "roommate is null");
        }
        this.roommate = roommate;
    }

    public ShoppingItemEntity(){}

    public ShoppingItemEntity(int id, String label, int quantity, RoommateEntity roommate) throws BackendException {
        setId(id);
        setLabel(label);
        setQuantity(quantity);
        setRoommate(roommate);
    }

    //Méthodes

    public static List<ShoppingItemEntity> getAll(ShoppingItemDao dao) throws BackendException
    {
        return dao.getAll();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingItemEntity that = (ShoppingItemEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}