package be.dikkenek.colocationbackend.dto;

public class LoginResponseDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String email, String firstname, String lastname, String phonenumber, Role role) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.role = role;
    }

    public enum Role {
        LANDLORD,
        ROOMMATE
    }
}
