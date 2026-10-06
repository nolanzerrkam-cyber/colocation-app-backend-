package be.dikkenek.colocationbackend.dto;

public class RegisterRequestDTO
{
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phonenumber;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public RegisterRequestDTO(){}

    public RegisterRequestDTO(String email, String password, String firstname, String lastname, String phonenumber) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
    }
}
