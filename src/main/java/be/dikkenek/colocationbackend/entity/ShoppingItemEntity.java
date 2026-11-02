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
    @Column(name = "libelle")
    private String libelle;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws BackendException {
        if(quantity <= 0)
        {
            throw new BackendException(Response.Status.BAD_REQUEST, "La quantité doit être strictement positive");
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

    public ShoppingItemEntity(int id, String libelle, int quantity, RoommateEntity roommate) throws BackendException {
        setId(id);
        setLibelle(libelle);
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