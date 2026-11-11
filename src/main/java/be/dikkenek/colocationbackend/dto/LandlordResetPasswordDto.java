package be.dikkenek.colocationbackend.dto;

// This DTO is used when the landlord needs to reset the password of a certain roommate.
// We don't take the old password from the roommate because we imagined that this problem will only occur when the roommate lost forever his old password.
public class LandlordResetPasswordDto
{
    private String landlordEmail;
    private String landlordPassword;
    private String roommateEmail;
    private String newPassword;

    public LandlordResetPasswordDto(String landlordEmail, String landlordPassword, String roommateEmail, String newPassword) {
        this.landlordEmail = landlordEmail;
        this.landlordPassword = landlordPassword;
        this.roommateEmail = roommateEmail;
        this.newPassword = newPassword;
    }

    public LandlordResetPasswordDto(){

    }

    public String getLandlordEmail() {
        return landlordEmail;
    }

    public void setLandlordEmail(String landlordEmail) {
        this.landlordEmail = landlordEmail;
    }

    public String getLandlordPassword() {
        return landlordPassword;
    }

    public void setLandlordPassword(String landlordPassword) {
        this.landlordPassword = landlordPassword;
    }

    public String getRoommateEmail() {
        return roommateEmail;
    }

    public void setRoommateEmail(String roommateEmail) {
        this.roommateEmail = roommateEmail;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
