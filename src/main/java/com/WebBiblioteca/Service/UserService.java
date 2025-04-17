package com.WebBiblioteca.Service;

import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getUsersList();
    }
    public void addUser(User user){
        userRepository.getUsersList().add(user);
    }
}
