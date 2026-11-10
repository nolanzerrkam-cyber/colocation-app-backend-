package be.dikkenek.colocationbackend.dto;

public class RentUpdateDto {
    private float amount;
    private int deadlineDay;

    public RentUpdateDto() {

    }

    public RentUpdateDto(float amount, int deadlineDay) {
        setAmount(amount);
        setDeadlineDay(deadlineDay);
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getDeadlineDay() {
        return deadlineDay;
    }

    public void setDeadlineDay(int deadlineDay) {
        this.deadlineDay = deadlineDay;
    }
}