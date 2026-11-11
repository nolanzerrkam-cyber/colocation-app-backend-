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
    @Column(name = "id_shopping", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_shopping;

    @Column(name = "label")
    private String label;
    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ROOMMATE_EMAIL")
    private RoommateEntity roommate;

    @ManyToOne
    @JoinColumn(name = "id_flatshare")
    private FlatshareEntity flatshare;

    public FlatshareEntity getFlatshare() {
        return flatshare;
    }

    public void setFlatshare(FlatshareEntity flatshare) throws BackendException {
        if(flatshare == null)
        {
            throw new BackendException(Response.Status.BAD_REQUEST,"flatshare cannot be null");
        }

        this.flatshare = flatshare;
    }

    public int getId_shopping() {
        return id_shopping;
    }

    public void setId_shopping(int id_shopping) throws BackendException {
        if(id_shopping < 0)
        {
            throw new BackendException(Response.Status.BAD_REQUEST,"id cannot be negative");
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws BackendException {
        if (quantity <= 0) {
            throw new BackendException(Response.Status.BAD_REQUEST, "Quantity must be positive");
        }
        this.quantity = quantity;
    }

    public RoommateEntity getRoommate() {
        return roommate;
    }

    public void setRoommate(RoommateEntity roommate) throws BackendException {
        if (roommate == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Roommate is null");

        if (!roommate.equals(this.roommate)) {
            if (this.roommate != null) this.roommate.getFlatshare().removeItem(this);
            this.roommate = roommate;
            roommate.getFlatshare().addItem(this);
        }
    }

    //Constructeurs

    public ShoppingItemEntity() {
    }

    public ShoppingItemEntity( String label, int quantity, RoommateEntity roommate) throws BackendException {
        setLabel(label);
        setQuantity(quantity);
        setRoommate(roommate);
        setFlatshare(roommate.getFlatshare());
    }

    public ShoppingItemEntity(int id_shopping, String label, int quantity, RoommateEntity roommate) throws BackendException {
        this(label,quantity,roommate);
        setId_shopping(id_shopping);
    }

    //Méthodes

    public static List<ShoppingItemEntity> getAll(ShoppingItemDao dao) throws BackendException {
        return dao.getAll();
    }

    public static ShoppingItemEntity get(ShoppingItemDao dao, int id) throws BackendException
    {
        return dao.get(id).orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShoppingItemEntity)) return false;
        ShoppingItemEntity that = (ShoppingItemEntity) o;
        return id_shopping == that.id_shopping && quantity == that.quantity && Objects.equals(label, that.label) && Objects.equals(roommate, that.roommate);
    }

    public boolean create(ShoppingItemDao dao) throws BackendException
    {
        return dao.create(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_shopping, label, quantity, roommate);
    }

    @Override
    public String toString() {
        return "ShoppingItemEntity{" +
                "id_shopping=" + id_shopping +
                ", label='" + label + '\'' +
                ", quantity=" + quantity +
                ", roommate=" + roommate +
                '}';
    }
}