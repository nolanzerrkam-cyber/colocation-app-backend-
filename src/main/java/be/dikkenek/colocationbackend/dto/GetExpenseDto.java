package be.dikkenek.colocationbackend.dto;

public class GetExpenseDto {

    private String label;

    private float price;

    private String date_expense;

    private String firstname;

    private String lastname;

    public GetExpenseDto(String label, float price, String date_expense, String firstname, String lastname)
    {
        setLabel(label);
        setPrice(price);
        setDate_expense(date_expense);
        setFirstname(firstname);
        setLastname(lastname);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDate_expense() {
        return date_expense;
    }

    public void setDate_expense(String date_expense) {
        this.date_expense = date_expense;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname (String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname (String lastname) {
        this.lastname = lastname;
    }

}
