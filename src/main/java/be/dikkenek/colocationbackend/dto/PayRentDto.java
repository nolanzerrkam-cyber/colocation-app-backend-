package be.dikkenek.colocationbackend.dto;

public class PayRentDto {
    private String email;
    private int month;
    private int year;

    public PayRentDto(String email, int month, int year) {
        setEmail(email);
        setMonth(month);
        setYear(year);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
