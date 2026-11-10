package be.dikkenek.colocationbackend.dto;

public class PasswordResetDTO
{
    private String email;
    private String newPassword;
    private String oldPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

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

    public PasswordResetDTO(String newPassword, String oldPassword, String email) {
        this.email = email;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

}
