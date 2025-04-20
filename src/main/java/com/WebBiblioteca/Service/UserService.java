package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.UserRepository;
import org.springframework.stereotype.Service;

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
       return userRepository.addUser(user);
    }
}
