package be.dikkenek.colocationbackend.dto;

public class RoommateResponseDto {
    private String email;
    private String firstname;
    private String lastname;
    private String phonenumber;

    public RoommateResponseDto(String email, String firstname, String lastname, String phonenumber) {
        setEmail(email);
        setFirstname(firstname);
        setLastname(lastname);
        setPhonenumber(phonenumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
