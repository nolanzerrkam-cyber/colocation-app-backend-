package be.dikkenek.colocationbackend.entity;

public class RoommateEntity extends UserEntity
{
    public RoommateEntity() {
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber) {
        super(email, password, firstname, lastname, phonenumber);
    }
}
