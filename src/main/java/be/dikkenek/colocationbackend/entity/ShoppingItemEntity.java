package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.ShoppingItemDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SHOPPING_ITEM")
public class ShoppingItemEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "label")
    private String label;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne
    private FlatshareEntity flatshare;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws BackendException {
        if (quantity <= 0) {
            throw new BackendException(Response.Status.BAD_REQUEST, "Quantity must be positive");
        }
        this.quantity = quantity;
    }

    public FlatshareEntity getFlatshare() {
        return flatshare;
    }

    public void setFlatshare(FlatshareEntity flatshare) throws BackendException {
        if (flatshare == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Flatshare is null");

        flatshare.addItem(this);
        this.flatshare = flatshare;
    }

    public ShoppingItemEntity() {
    }

    public ShoppingItemEntity(int id, String label, int quantity, FlatshareEntity flatshare) throws BackendException {
        setId(id);
        setLabel(label);
        setQuantity(quantity);
        setFlatshare(flatshare);
    }

    //Méthodes

    public static List<ShoppingItemEntity> getAll(ShoppingItemDao dao) throws BackendException {
        return dao.getAll();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShoppingItemEntity)) return false;
        ShoppingItemEntity that = (ShoppingItemEntity) o;
        return id == that.id && quantity == that.quantity && Objects.equals(label, that.label) && Objects.equals(flatshare, that.flatshare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, quantity, flatshare);
    }

    @Override
    public String toString() {
        return "ShoppingItemEntity{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", quantity=" + quantity +
                ", flatshare=" + flatshare +
                '}';
    }
}