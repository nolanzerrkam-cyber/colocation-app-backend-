package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Entity
@Table(name = "shopping_quantity")
public class ShoppingQuantityEntity {
    @Id
    @Column(name = "id_quantity", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_quantity;

    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingItemEntity item;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "id_flatshare")
    private FlatshareEntity flatshare;
    /*constructor*/

    public ShoppingQuantityEntity() {
    }

    public ShoppingQuantityEntity(ShoppingItemEntity item, int quantity, FlatshareEntity flatshare) throws BackendException {
        setShoppingItem(item);
        setQuantity(quantity);
        setFlatshare(flatshare);
    }

    /*getter and setter*/
    public int getId_quantity() {
        return id_quantity;
    }

    public void setId_quantity(int id_quantity) throws BackendException {
        if (id_quantity < 0) {
            throw new BackendException(Response.Status.BAD_REQUEST, "id cannot be negative");
        }
        this.id_quantity = id_quantity;
    }

    public ShoppingItemEntity getItem() {
        return item;
    }

    public void setShoppingItem(ShoppingItemEntity item) throws BackendException {
        if (item == null) {
            throw new BackendException(Response.Status.BAD_REQUEST, "shopping item cannot be null");
        }
        this.item = item;
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
        if (flatshare == null) {
            throw new BackendException(Response.Status.BAD_REQUEST, "flatshare cannot be null");
        }
        this.flatshare = flatshare;
        flatshare.addItem(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingQuantityEntity that = (ShoppingQuantityEntity) o;
        return getId_quantity() == that.getId_quantity() && getQuantity() == that.getQuantity() && Objects.equals(getItem(), that.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_quantity(), getItem(), getQuantity());
    }

    @Override
    public String toString() {
        return "ShoppingQuantityEntity{" +
                "quantity=" + quantity +
                ", item=" + item +
                ", id_quantity=" + id_quantity +
                '}';
    }
}
