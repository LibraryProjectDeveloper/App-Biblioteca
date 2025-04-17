package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.User;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class UserRepository{
    private List<User> usersList = new LinkedList<>();

    public List<User> getUsersList(){
        return usersList;
    }
}
