package be.dikkenek.colocationbackend.dto;

public class RegisterRequestDto
{
    private String email;
    private String firstname;
    private String lastname;
    private String phonenumber;

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public RegisterRequestDto(){}

    public RegisterRequestDto(String email, String firstname, String lastname, String phonenumber) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
    }
}
