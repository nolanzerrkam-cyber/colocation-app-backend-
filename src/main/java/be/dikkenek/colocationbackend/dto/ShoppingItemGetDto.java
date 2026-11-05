package be.dikkenek.colocationbackend.dto;

public class ShoppingItemGetDto
{
    private int id;
    private String label;
    private int quantity;
    private String roommateId;

    public String getRoommateId() {
        return roommateId;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public int getQuantity() {
        return quantity;
    }

    public ShoppingItemGetDto(){}

    public ShoppingItemGetDto(int id, String label, int quantity, String roommateId) {
        this.id = id;
        this.label = label;
        this.quantity = quantity;
        this.roommateId = roommateId;
    }
}
