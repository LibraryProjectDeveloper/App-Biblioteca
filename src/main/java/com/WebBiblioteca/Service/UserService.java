package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.UserRepository;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return null; // User with this email already exists
        }
        user.setDateRegistered(LocalDateTime.now());
        user.setState(true);
        return userRepository.save(user);
    }
    /*
    public User loginUser(String email, String password ){
        User user = userRepository.getUserByEmail(email);
        if(user != null && user.getPassword().equals(password) && user.getRole().getNameRol() == Role.LIBRARIAN && user.getState()){
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
            if(user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if(user.getLastname() != null) {
                existingUser.setLastname(user.getLastname());
            }
            if(user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if(user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if(user.getAddress() != null) {
                existingUser.setAddress(user.getAddress());
            }
            if(user.getDNI() != null) {
                existingUser.setDNI(user.getDNI());
            }
            if(user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            }
            if(user.getState() != null) {
                existingUser.setState(user.getState());
            }
            if(user.getRole() != null) {
                existingUser.setRole(user.getRole());
            }
            if(user.getDateRegistered() != null) {
                existingUser.setDateRegistered(user.getDateRegistered());
            }
            return userRepository.updateUser(id,existingUser);
        }else{
            return null;
        }
    }
     */
}
