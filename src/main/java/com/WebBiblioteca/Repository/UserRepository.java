package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.User;
import org.springframework.stereotype.Repository;

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
    public User getUserByEmail(String email) {
        for (User user : usersMap.values()) {
            if (email != null && email.equals(user.getEmail())) {
                return user;
            }
        }
        return null;
    }

    //agregar usuario
    public User addUser(User user){
        user.setCode(idGenerator.getAndIncrement());
        usersMap.put(user.getCode(), user);
        return user;
    }
    // actualizar usuario
    public User updateUser(Long id, User user){
        if (usersMap.containsKey(id)) {
            usersMap.put(id, user);
            return user;
        }
        return null;
    }
    // eliminar usuario
    public void deleteUser(Long id){
        usersMap.remove(id);
    }
    // acutualizar algunos campos
    public User partialUpdate(Long id, User user){
        if (usersMap.containsKey(id)) {
            usersMap.put(id, user);
            return user;
        }
        return null;
    }
}
