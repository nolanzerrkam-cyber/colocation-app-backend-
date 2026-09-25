package be.dikkenek.colocationbackend.service;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.LoginResponseDTO;
import be.dikkenek.colocationbackend.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserService
{
    private final UserDao userDao;

    public UserService(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public LoginResponseDTO createUser(UserEntity entity)
    {
        if(userDao.get(entity.getEmail()).isPresent())
        {
            throw new IllegalArgumentException("Invalid email");
        }

        if(!userDao.create(entity))
        {
            throw new RuntimeException("Error in user creation");
        }

        return new LoginResponseDTO(entity.getEmail(),entity.getFirstname(),entity.getLastname(),entity.getPhonenumber());
    }

    public List<LoginResponseDTO> getAll()
    {
        List<UserEntity> entities = userDao.getAll();

        if(entities.isEmpty())
        {
            throw new RuntimeException("No users found");
        }

        List<LoginResponseDTO> dtos = new ArrayList<>();
        entities.forEach(e ->
        {
            dtos.add(new LoginResponseDTO(e.getEmail(),e.getFirstname(),e.getLastname(),e.getPhonenumber()));
        });

        return dtos;
    }

    public LoginResponseDTO login(UserEntity entity)
    {
        UserEntity found = userDao.get(entity.getEmail()).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if(!found.getPassword().equals(entity.getPassword()))
        {
            throw new SecurityException("Invalid credentials");
        }

        return new LoginResponseDTO(entity.getEmail(),entity.getFirstname(),entity.getLastname(),entity.getPhonenumber());
    }

    public void deleteUser(String id)
    {
        if(!userDao.delete(id))
        {
            throw new IllegalArgumentException("Invalid id");
        }
    }

    public void updateUser(UserEntity entity)
    {
        if(!userDao.update(entity))
        {
            throw new RuntimeException("Update failed");
        }
    }
}
