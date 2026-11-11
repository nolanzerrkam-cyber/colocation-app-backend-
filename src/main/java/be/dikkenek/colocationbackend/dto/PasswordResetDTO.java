package be.dikkenek.colocationbackend.dto;

// This is the DTO for the password reset, but for the roommate.
// We are asking for the old password for security reasons, and if the old password is correct and the new password is defined correctly, we
// can use this DTO for updating our password.

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
