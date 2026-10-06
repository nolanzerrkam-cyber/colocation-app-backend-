package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginRegisterResponseDTO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = RoommateEntity.class, name = "Roommate"),
        @JsonSubTypes.Type(value = LandlordEntity.class, name = "Landlord")})
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

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
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

        public LoginRegisterResponseDTO create(UserDao userDao)
        {
            if(userDao.get(this.getEmail()).isPresent())
            {
                throw new IllegalArgumentException("Invalid email");
            }

            if(!userDao.create(this))
            {
                throw new RuntimeException("Error in user creation");
            }

            return new LoginRegisterResponseDTO(this.getEmail(),this.getFirstname(),this.getLastname(),this.getPhonenumber());
        }

        public static UserEntity get(UserDao userDao,String email)
        {
            return userDao.get(email).orElseThrow(() -> new RuntimeException("No user found"));
        }

        public static List<LoginRegisterResponseDTO> getAll(UserDao userDao)
        {
            List<UserEntity> entities = userDao.getAll();

            if(entities.isEmpty())
            {
                throw new RuntimeException("No users found");
            }

            List<LoginRegisterResponseDTO> dtos = new ArrayList<>();
            entities.forEach(e ->
            {
                dtos.add(new LoginRegisterResponseDTO(e.getEmail(),e.getFirstname(),e.getLastname(),e.getPhonenumber()));
            });

            return dtos;
        }

        public LoginRegisterResponseDTO login(UserDao userDao)
        {
            UserEntity found = userDao.get(this.getEmail()).orElseThrow(() -> new RuntimeException("Invalid credentials"));

            if(!found.getPassword().equals(this.getPassword()))
            {
                throw new SecurityException("Invalid credentials");
            }

            return new LoginRegisterResponseDTO(found.getEmail(),found.getFirstname(),found.getLastname(),found.getPhonenumber());
        }

        public void delete(UserDao userDao)
        {
            if(!userDao.delete(this.getEmail()))
            {
                throw new IllegalArgumentException("Invalid id");
            }
        }

        public void update(UserDao userDao)
        {
            if(!userDao.update(this))
            {
                throw new RuntimeException("Update failed");
            }
        }
}
