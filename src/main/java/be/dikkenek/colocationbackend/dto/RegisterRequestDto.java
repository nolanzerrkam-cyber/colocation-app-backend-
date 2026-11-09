package be.dikkenek.colocationbackend.dto;

public class RegisterRequestDto
{
    private String email;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private String rawPassword;

    // Getter

    public String getRawPassword() {
        return rawPassword;
    }
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

    // setter
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
    public void setRawPassword(String rawPassword) { this.rawPassword = rawPassword; }

    public RegisterRequestDto(){}

    public RegisterRequestDto(String email, String firstname, String lastname, String phonenumber, String rawPassword)
    {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.rawPassword = rawPassword;
    }
}
