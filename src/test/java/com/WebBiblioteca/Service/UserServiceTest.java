package com.WebBiblioteca.Service;

import com.WebBiblioteca.Config.EncodeConfig;
import com.WebBiblioteca.DTO.Usuario.UserRequest;
import com.WebBiblioteca.DTO.Usuario.UserResponse;
import com.WebBiblioteca.Model.Rol;
import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.RolRepository;
import com.WebBiblioteca.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private EncodeConfig encodeConfig;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user = mock(User.class);
        Rol rol = mock(Rol.class);
        when(user.getCode()).thenReturn(1L);
        when(user.getName()).thenReturn("Juan");
        when(user.getLastname()).thenReturn("Perez");
        when(user.getEmail()).thenReturn("juan@mail.com");
        when(user.getPhone()).thenReturn("999");
        when(user.getAddress()).thenReturn("Calle 1");
        when(user.getDNI()).thenReturn("12345678");
        when(user.getPassword()).thenReturn("pass");
        when(user.getState()).thenReturn(true);
        when(user.getDateRegistered()).thenReturn(LocalDateTime.now());
        when(user.getRol()).thenReturn(rol);
        when(rol.getIdRol()).thenReturn(2L);
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserResponse> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("Juan", result.getFirst().getName());
    }

    @Test
    void testAddUser() {
        var encoder = mock(org.springframework.security.crypto.password.PasswordEncoder.class);
        UserRequest req = mock(UserRequest.class);
        Rol rol = new Rol();
        rol.setIdRol(1L);
        when(req.getIdRol()).thenReturn(1L);
        when(req.getEmail()).thenReturn("test@mail.com");
        when(req.getDNI()).thenReturn("12345678");
        when(rolRepository.findByIdRol(1L)).thenReturn(Optional.of(rol));
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(userRepository.findByDNI("12345678")).thenReturn(Optional.empty());
        when(encodeConfig.passwordEncoder()).thenReturn(encoder);
        when(encoder.encode(anyString())).thenReturn("encodedPass");
        User savedUser = new User();
        savedUser.setRol(rol);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        assertDoesNotThrow(() -> userService.addUser(req));
    }

    @Test
    void testUpdateUser() {
        var encoder = mock(org.springframework.security.crypto.password.PasswordEncoder.class);
        UserRequest req = mock(UserRequest.class);
        User user = new User();
        Rol rol = new Rol();
        rol.setIdRol(1L);
        when(req.getIdUsuario()).thenReturn(1L);
        when(req.getName()).thenReturn("NuevoNombre");
        when(req.getLastname()).thenReturn("NuevoApellido");
        when(req.getEmail()).thenReturn("nuevo@mail.com");
        when(req.getPhone()).thenReturn("123456789");
        when(req.getAddress()).thenReturn("Nueva Direccion");
        when(req.getDNI()).thenReturn("87654321");
        when(req.getPassword()).thenReturn("nuevaPass");
        when(req.getState()).thenReturn(true);
        when(req.getIdRol()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(rolRepository.findByIdRol(1L)).thenReturn(Optional.of(rol));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(encodeConfig.passwordEncoder()).thenReturn(encoder);
        when(encoder.encode(anyString())).thenReturn("encodedPass");
        assertDoesNotThrow(() -> userService.updateUser(req, 1L));
        assertEquals("NuevoNombre", user.getName());
        assertEquals("NuevoApellido", user.getLastname());
        assertEquals("nuevo@mail.com", user.getEmail());
        assertEquals("123456789", user.getPhone());
        assertEquals("Nueva Direccion", user.getAddress());
        assertEquals("87654321", user.getDNI());
        assertTrue(user.getState());
    }

    @Test
    void testLoadUserByUsername() {
        User user = mock(User.class);
        Rol rol = mock(Rol.class);
        when(userRepository.findByEmail("mail@mail.com")).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn("mail@mail.com");
        when(user.getPassword()).thenReturn("pass");
        when(user.getRol()).thenReturn(rol);
        when(rol.getNameRol()).thenReturn(Role.USER);
        UserDetails details = userService.loadUserByUsername("mail@mail.com");
        assertEquals("mail@mail.com", details.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmail("no@mail.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("no@mail.com"));
    }
}
