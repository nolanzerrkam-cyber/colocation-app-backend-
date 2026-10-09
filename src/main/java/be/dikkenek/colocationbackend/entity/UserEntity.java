package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity
{
        //TODO: ajouter la verif que l email soit au format valide
        @Id
        private String email;
        private String password;
        private String firstname;
        private String lastname;
        private String phonenumber;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email)
        {
            if(!verifyEmailFormat(email))
            {
                throw new IllegalArgumentException("Invalid email");
            }
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password)
        {
            if (password == null || password.length() < 8)
            {
                throw new IllegalArgumentException("Password must be at least 8 characters");
            }

            this.password = password;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname)
        {
            if(firstname == null )
            {
                throw new IllegalArgumentException("Firstname cannot be null");
            }

            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname)
        {
            if(lastname == null )
            {
                throw new IllegalArgumentException("Lastname cannot be null");
            }
            this.lastname = lastname;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber)
        {
            if(phonenumber == null )
            {
                throw new IllegalArgumentException("Phonenumber cannot be null");
            }
            this.phonenumber = phonenumber;
        }

        public UserEntity(){}

        public UserEntity(String email, String password, String firstname, String lastname, String phonenumber)
        {
            this.email = email;
            this.password = password;
            this.firstname = firstname;
            this.lastname = lastname;
            this.phonenumber = phonenumber;
        }

        //Business Logic

        public UserEntity create(UserDao userDao)
        {
            if(userDao.get(this.getEmail()).isPresent())
            {
                throw new IllegalArgumentException("Invalid email");
            }

            if(!userDao.create(this))
            {
                throw new RuntimeException("Error in user creation");
            }

            return this;
        }

        public static UserEntity get(UserDao userDao,String email)
        {
            return userDao.get(email).orElse(null);
        }

        public static List<UserEntity> getAll(UserDao userDao)
        {
            return userDao.getAll();
        }

        public boolean delete(UserDao userDao)
        {
            return userDao.delete(this.getEmail());
        }

        public boolean update(UserDao userDao)
        {
            return userDao.update(this);
        }

        //Business logic

        public boolean verifyPassword(String password)
        {
            return this.getPassword().equals(password);
        }

        private boolean verifyEmailFormat(String email)
        {
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");

            if (email == null)
            {
                return false;
            }

            String trimmed = email.trim();

            return trimmed.length() <= 254 && emailPattern.matcher(trimmed).matches();
        }
}
