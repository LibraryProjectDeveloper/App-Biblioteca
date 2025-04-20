package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public HashMap<Long, User> getAllUsers() {
        return userRepository.getUsersList();
    }
    public User addUser(User user){
       user.setDateRegistered(LocalDateTime.now());
       user.setState(true);
       return userRepository.addUser(user);
    }
    public User loginUser(String email, String password){
        User user = userRepository.getUserByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
    public boolean deleteUser(Long id){
        if(getAllUsers().get(id) !=null){
            userRepository.deleteUser(id);
            return true;
        }else{
            return false;
        }
    }
    public User updateUser(User user,Long id){
        User existingUser = userRepository.getUserById(id);
        if ( existingUser != null){
            existingUser.setName(user.getName());
            existingUser.setLastname(user.getLastname());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setDNI(user.getDNI());
            existingUser.setPassword(user.getPassword());
            existingUser.setState(user.getState());
            return userRepository.updateUser(id,existingUser);
        }else{
            return null;
        }
    }
    public User updatePartialUser(Long id, User partialUser){
        User existingUser = userRepository.getUserById(id);
        if (existingUser != null) {
            if (partialUser.getName() != null) existingUser.setName(partialUser.getName());
            if (partialUser.getLastname() != null) existingUser.setLastname(partialUser.getLastname());
            if (partialUser.getEmail() != null) existingUser.setEmail(partialUser.getEmail());
            if (partialUser.getPhone() != null) existingUser.setPhone(partialUser.getPhone());
            if (partialUser.getAddress() != null) existingUser.setAddress(partialUser.getAddress());
            if (partialUser.getDNI() != null) existingUser.setDNI(partialUser.getDNI());
            if (partialUser.getPassword() != null) existingUser.setPassword(partialUser.getPassword());
            if (partialUser.getState() != null) existingUser.setState(partialUser.getState());
            return userRepository.partialUpdate(id, existingUser);
        }
        return null;
    }
}
