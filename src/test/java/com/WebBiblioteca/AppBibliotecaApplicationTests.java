package com.WebBiblioteca;

import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppBibliotecaApplicationTests {
    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    void setUp(){
        userRepository = new UserRepository();
        testUser = new User();
        testUser.setName("John");
        testUser.setLastname("Doe");
        testUser.setEmail("john@gmail.com");
        testUser.setPhone("985689574");
        testUser.setAddress("123 Main St");
        testUser.setDNI("88995554");
        testUser.setPassword("password123");
        testUser.setState(true);
        testUser.setRole(Role.ADMIN);

        userRepository.addUser(new User(null,"Maria","martines",
                "maria@gmail.com","985689574","123 Main St","88995554",
                "password123",null, null,Role.ADMIN));

        userRepository.addUser(new User(null, "Juan", "Perez",
                "juan.perez@example.com", "600123456", "456 Oak Ave", "77889900",
                "juanPass123", null, null, Role.USER));

        userRepository.addUser(new User(null, "Sofia", "Ramirez",
                "sofia.r@inactive.net", "688333999", "951 Birch Ct", "33445566",
                "sofia2020", null, null, Role.USER));

        userRepository.addUser(new User(null, "Miguel", "Diaz",
                "miguel.d@suspended.org", "699888777", "357 Spruce Way", "22334455",
                "miguel123", null , null, Role.USER));
    }

    @Test
    void addUser() {
        User addUser = userRepository.addUser(testUser);

        assertNotNull(addUser.getCode());
        assertEquals(5L, addUser.getCode());
        User getUser = userRepository.getUserById(addUser.getCode());
        assertEquals(3L, getUser.getCode());
    }
    @Test
    void contextLoads() {
        System.out.println(userRepository.getUsersList().isEmpty());
        userRepository.getUsersList().forEach((key, value) -> {
            System.out.println("ID: " + key);
            System.out.println("User: " + value);
            System.out.println("Name: " + value.getName());
            System.out.println("Last Name: " + value.getLastname());
            System.out.println("Email: " + value.getEmail());
            System.out.println("Phone: " + value.getPhone());
            System.out.println("Address: " + value.getAddress());
            System.out.println("DNI: " + value.getDNI());
            System.out.println("Password: " + value.getPassword());
            System.out.println("State: " + value.getState());
            System.out.println("Date Registered: " + value.getDateRegistered().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("Role: " + value.getRole());
            System.out.println("------------------------------");
        });
    }



}
