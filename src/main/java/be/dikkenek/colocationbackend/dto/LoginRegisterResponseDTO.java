package be.dikkenek.colocationbackend.dto;

public class LoginRegisterResponseDTO
{
    private String email;
    private String firstname;
    private String lastname;
    private String phonenumber;

    public String getEmail()
    {
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

    public LoginRegisterResponseDTO(){}

    public LoginRegisterResponseDTO(String email, String firstname, String lastname, String phonenumber)
    {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
    }
}
