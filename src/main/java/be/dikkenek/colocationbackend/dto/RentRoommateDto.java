package be.dikkenek.colocationbackend.dto;

public class RentRoommateDto {
    private float rent;
    private boolean isRentPaid;

    public RentRoommateDto(float rent, boolean isRentPaid) {
        setRent(rent);
        setRentPaid(isRentPaid);
    }

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
        this.rent = rent;
    }

    public boolean isRentPaid() {
        return isRentPaid;
    }

    public void setRentPaid(boolean rentPaid) {
        isRentPaid = rentPaid;
    }
}
