package be.dikkenek.colocationbackend.dto;

public class LoginRequestDTO
{
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequestDTO(String email, String password)
    {
        this.email = email;
        this.password = password;
    }
}
