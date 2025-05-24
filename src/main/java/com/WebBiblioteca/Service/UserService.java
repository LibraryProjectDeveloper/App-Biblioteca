package com.WebBiblioteca.Service;

import com.WebBiblioteca.Config.EncodeConfig;
import com.WebBiblioteca.DTO.Usuario.UserRequest;
import com.WebBiblioteca.DTO.Usuario.UserResponse;
import com.WebBiblioteca.Exception.DuplicateResourceException;
import com.WebBiblioteca.Exception.ResourceNotFoundException;
import com.WebBiblioteca.Model.Rol;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.RolRepository;
import com.WebBiblioteca.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService  implements UserDetailsService {
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final EncodeConfig encodeConfig;

    public UserService(UserRepository userRepository, RolRepository rolRepository, EncodeConfig encodeConfig) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.encodeConfig = encodeConfig;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().
                stream().map(user -> new UserResponse(
                        user.getCode(),
                        user.getName(),
                        user.getLastname(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getAddress(),
                        user.getDNI(),
                        user.getPassword(),
                        user.getState(),
                        user.getDateRegistered(),
                        user.getRol().getIdRol()
                )).toList();
    }

    @Transactional
    public void addUser(UserRequest user) {
        Rol rol = rolRepository.findByIdRol(user.getIdRol()).orElseThrow(() -> new ResourceNotFoundException("Rol","id",user.getIdRol()));
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Usuario", "email", user.getEmail());
        }
        if (userRepository.findByDNI(user.getDNI()).isPresent()) {
            throw new DuplicateResourceException("Usuario", "DNI", user.getDNI());
        }
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setLastname(user.getLastname());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setAddress(user.getAddress());
        newUser.setDNI(user.getDNI());
        newUser.setPassword(encodeConfig.passwordEncoder().encode(user.getPassword()));
        newUser.setState(true);
        newUser.setDateRegistered(LocalDateTime.now());
        newUser.setRol(rol);
        userRepository.save(newUser);
    }
    public void updateUser(UserRequest user) {
        User userToUpdate = userRepository.findById(user.getIdUsuario()).orElseThrow(()-> new ResourceNotFoundException("Usuario","id",user.getIdUsuario()));
        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        if (user.getLastname() != null) {
            userToUpdate.setLastname(user.getLastname());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            userToUpdate.setPhone(user.getPhone());
        }
        if (user.getAddress() != null) {
            userToUpdate.setAddress(user.getAddress());
        }
        if (user.getDNI() != null) {
            userToUpdate.setDNI(user.getDNI());
        }
        if (user.getPassword() != null) {
            userToUpdate.setPassword(encodeConfig.passwordEncoder().encode(user.getPassword()));
        }
        if (user.getState() != null) {
            userToUpdate.setState(user.getState());
        }
        if (user.getIdRol() != null) {
            Rol rol = rolRepository.findByIdRol(user.getIdRol()).orElseThrow(() -> new ResourceNotFoundException("Rol","id",user.getIdRol()));
            userToUpdate.setRol(rol);
        }
        userRepository.save(userToUpdate);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_"+user.getRol().getNameRol())
                .build();
    }
}
