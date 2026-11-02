package be.dikkenek.colocationbackend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class ShoppingItemGetDto
{
    private int id;
    private String libelle;
    private int quantity;
    private String roommateId;

    public String getRoommateId() {
        return roommateId;
    }

    public int getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getQuantity() {
        return quantity;
    }



    public ShoppingItemGetDto(){}

    public ShoppingItemGetDto(int id, String libelle, int quantity, String roommateId) {
        this.id = id;
        this.libelle = libelle;
        this.quantity = quantity;
        this.roommateId = roommateId;
    }
}
