package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository{
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final HashMap<Long,User> usersMap = new HashMap<>();

    //listar usuarios
    public HashMap<Long,User> getUsersList(){
        return usersMap;
    }

    //listar usuarios por su estado
    public Map<Long,User> getUsersByState(Boolean state){
        return usersMap.entrySet().stream()
                .filter(entry -> entry.getValue().getState().equals(state))
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    //buscar usuario por su id
    public User getUserById(Long id){
        return usersMap.get(id);
    }

    //buscar usuario por su email
    public User getUserByEmail(String email){
       return usersMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    //agregar usuario
    public User addUser(User user){
        Long id = idGenerator.getAndIncrement();
        user.setCode(id);
        user.setDateRegistered(LocalDateTime.now());
        user.setState(true);
        usersMap.put(id, user);
        return user;
    }

    //loguear usuario
    public User loginUser(String email, String password){
        return usersMap.values().stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password) && user.getState())
                .findFirst()
                .orElse(null);
    }

    // actualizar usuario
    public User updateUser(Long id, User user){
        return usersMap.computeIfPresent(id, (key, existingUser) -> {
            existingUser.setName(user.getName());
            existingUser.setLastname(user.getLastname());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setDNI(user.getDNI());
            existingUser.setPassword(user.getPassword());
            existingUser.setState(user.getState());
            return existingUser;
        });

    }

    // eliminar usuario
    public void deleteUser(Long id){
        usersMap.remove(id);
    }
    // acutualizar algunos campos
    public User partialUpdate(Long id, User user){
        return usersMap.computeIfPresent(id, (key, existingUser) -> {
            if (user.getName() != null) existingUser.setName(user.getName());
            if (user.getLastname() != null) existingUser.setLastname(user.getLastname());
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
            if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
            if (user.getDNI() != null) existingUser.setDNI(user.getDNI());
            if (user.getPassword() != null) existingUser.setPassword(user.getPassword());
            if (user.getState() != null) existingUser.setState(user.getState());
            return existingUser;
        });
    }

}
