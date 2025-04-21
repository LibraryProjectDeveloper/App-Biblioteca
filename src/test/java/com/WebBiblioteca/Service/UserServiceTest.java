package com.WebBiblioteca.Service;
import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);
    @Test
    void testAddUser() {
        User user = new User(1L,"Renato","Ballena","renato@gmail.com","987654321","Manco capac 249","87654321","clave123",true, LocalDateTime.now(),Role.USER);
        User result = userService.addUser(user);
        assertEquals("Renato", result.getName());
        assertEquals("Ballena", result.getLastname());
        assertEquals("renato@gmail.com",result.getEmail());
        assertEquals("987654321",result.getPhone());
        assertEquals("Manco capac 249",result.getAddress());
        assertEquals("87654321",result.getDNI());
        assertEquals("clave123",result.getPassword());
        assertEquals(true,result.getState());
        assertEquals(Role.USER,result.getRole());
        assertNotNull(result);
    }
    @Test
    void testLoginUserSuccess() {
        User user = new User(null, "Renato", "Ballena", "renato@gmail.com", "987654321",
                "Manco capac 249", "87654321", "clave123", true, LocalDateTime.now(), Role.USER);
        userService.addUser(user);
        User result = userService.loginUser("renato@gmail.com", "clave123");
        assertNotNull(result);
        assertEquals("Renato", result.getName());
    }
    @Test
    void testLoginUserFailure() {
        User user = new User(null, "Renato", "Ballena", "renato@gmail.com", "987654321",
                "Manco capac 249", "87654321", "clave123", true, LocalDateTime.now(), Role.USER);
        userService.addUser(user);
        User result = userService.loginUser("renato@gmail.com", "claveIncorrecta");
        assertNull(result);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(null, "Renato", "Ballena", "renato@gmail.com", "987654321",
                "Manco capac 249", "87654321", "clave123", true, LocalDateTime.now(), Role.USER);
        User user2 = new User(null, "Ana", "López", "ana@gmail.com", "987654322",
                "Av. Principal 123", "87654322", "clave456", true, LocalDateTime.now(), Role.ADMIN);
        userService.addUser(user1);
        userService.addUser(user2);
        HashMap<Long,User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUserSuccess() {
        User user = new User(null, "Renato", "Ballena", "renato@gmail.com", "987654321",
                "Manco capac 249", "87654321", "clave123", true, LocalDateTime.now(), Role.USER);
        User savedUser = userService.addUser(user);
        User updatedInfo = new User();
        updatedInfo.setName("Renato Actualizado");
        updatedInfo.setPhone("999888777");
        User result = userService.updateUser(updatedInfo, savedUser.getCode());
        assertNotNull(result);
        assertEquals("Renato Actualizado", result.getName());
        assertEquals("999888777", result.getPhone());
        assertEquals("Ballena", result.getLastname());
    }

    @Test
    void testUpdateUserUserNotFound() {
        User updatedInfo = new User();
        updatedInfo.setName("Renato Actualizado");
        User result = userService.updateUser(updatedInfo, 999L);
        assertNull(result);
    }

    @Test
    void testDeleteUserSuccess() {
        User user = new User(null, "Renato", "Ballena", "renato@gmail.com", "987654321",
                "Manco capac 249", "87654321", "clave123", true, LocalDateTime.now(), Role.USER);
        User savedUser = userService.addUser(user);
        boolean result = userService.deleteUser(savedUser.getCode());
        assertTrue(result);
        assertNull(userService.getAllUsers().get(savedUser.getCode()));
    }
    @Test
    void testDeleteUserUserNotFound() {
        boolean result = userService.deleteUser(999L);
        assertFalse(result);
    }
}
