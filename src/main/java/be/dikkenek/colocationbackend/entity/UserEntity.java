package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity {
    @Id
    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "phonenumber", nullable = false)
    private String phonenumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws BackendException {
        final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if(!Pattern.compile(emailRegex).matcher(email).matches())
            throw new BackendException(Response.Status.BAD_REQUEST, "Email is not valid");
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws BackendException {
        if(password.length() < 8)
            throw new BackendException(Response.Status.BAD_REQUEST, "Password must contain at least 8 characters");
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) throws BackendException {
        if(firstname.isEmpty())
            throw new BackendException(Response.Status.BAD_REQUEST, "Firstname cannot be empty");
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) throws BackendException {
        if(lastname.isEmpty())
            throw new BackendException(Response.Status.BAD_REQUEST, "Lastname cannot be empty");
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) throws BackendException {
        if(phonenumber.isEmpty())
            throw new BackendException(Response.Status.BAD_REQUEST, "Phonenumber cannot be empty");
        this.phonenumber = phonenumber;
    }

    public UserEntity() {
    }

    public UserEntity(String email, String password, String firstname, String lastname, String phonenumber) throws BackendException {
        setEmail(email);
        setPassword(password);
        setFirstname(firstname);
        setLastname(lastname);
        setPhonenumber(phonenumber);
    }

    public boolean create(UserDao userDao) throws BackendException {
        return userDao.create(this);
    }

    public static UserEntity get(UserDao userDao, String email) throws BackendException {
        return userDao.get(email).orElse(null);
    }

    public static List<UserEntity> getAll(UserDao userDao) throws BackendException {
        return userDao.getAll();
    }

    public boolean delete(UserDao userDao) throws BackendException {
        return userDao.delete(this.getEmail());
    }

    public boolean update(UserDao userDao) throws BackendException {
        return userDao.update(this);
    }

    public boolean verifyPassword(String password) {
        return this.getPassword().equals(password);
    }
}
