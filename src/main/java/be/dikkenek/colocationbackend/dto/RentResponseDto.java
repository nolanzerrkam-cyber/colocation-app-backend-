package be.dikkenek.colocationbackend.dto;

public class RentResponseDto
{
    private int id;
    private float price;
    private int deadlineDay;

    public RentResponseDto(int id, float price, int deadlineDay)
    {
        setId(id);
        setPrice(price);
        setDeadlineDay(deadlineDay);
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

    public int getDeadlineDay() {
        return deadlineDay;
    }

    public void setDeadlineDay(int deadlineDay) {
        this.deadlineDay = deadlineDay;
    }
}
