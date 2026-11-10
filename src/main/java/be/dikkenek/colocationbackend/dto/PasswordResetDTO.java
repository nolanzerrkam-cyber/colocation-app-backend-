package be.dikkenek.colocationbackend.dto;

public class PasswordResetDTO
{
    private String email;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public PasswordResetDTO()
    {

    }

    public PasswordResetDTO(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
}
