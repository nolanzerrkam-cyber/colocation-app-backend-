package be.dikkenek.colocationbackend.dto;

public class RentUpdateDto
{
    private float price;

    public RentUpdateDto()
    {

    }

    public RentUpdateDto(float price)
    {
        setPrice(price);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}