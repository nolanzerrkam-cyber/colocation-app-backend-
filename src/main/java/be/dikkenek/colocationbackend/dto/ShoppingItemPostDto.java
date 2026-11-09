package be.dikkenek.colocationbackend.dto;

public class ShoppingItemPostDto {
    private String label;
    private int quantity;
    private String roommateId;

    public String getRoommateId() {
        return roommateId;
    }

    public String getLabel() {
        return label;
    }

    public int getQuantity() {
        return quantity;
    }



    public ShoppingItemPostDto(){}

    public ShoppingItemPostDto(String label, int quantity, String roommateId) {
        this.label = label;
        this.quantity = quantity;
        this.roommateId = roommateId;
    }
}
