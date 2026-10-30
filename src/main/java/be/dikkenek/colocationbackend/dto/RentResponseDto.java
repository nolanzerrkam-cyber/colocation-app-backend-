package be.dikkenek.colocationbackend.dto;

public class RentResponseDto
{
    private int id;
    private float price;
    private String lEmail;

    public RentResponseDto(int id, float price, String lEmail)
    {
        setId(id);
        setPrice(price);
        setlEmail(lEmail);
    }

    public RentResponseDto()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getlEmail() {
        return lEmail;
    }

    public void setlEmail(String lEmail) {
        this.lEmail = lEmail;
    }
}
