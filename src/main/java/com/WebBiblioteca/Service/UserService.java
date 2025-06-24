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
                        user.getRol().getNameRol().name()
                )).toList();
    }
    public List<UserResponse> getAllUsersByState(Boolean state) {
        return userRepository.findByState(state).
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
                        user.getRol().getNameRol().name()
                )).toList();
    }

    @Transactional
    public UserResponse addUser(UserRequest user) {
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
        User userCreating = userRepository.save(newUser);
        return new UserResponse(
                userCreating.getCode(),
                userCreating.getName(),
                userCreating.getLastname(),
                userCreating.getEmail(),
                userCreating.getPhone(),
                userCreating.getAddress(),
                userCreating.getDNI(),
                userCreating.getPassword(),
                userCreating.getState(),
                userCreating.getDateRegistered(),
                userCreating.getRol().getNameRol().name()
        );
    }
    @Transactional
    public UserResponse updateUser(UserRequest user,Long id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Usuario","id",id));
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
        User userUpdate = userRepository.save(userToUpdate);
        return new UserResponse(
                userUpdate.getCode(),
                userUpdate.getName(),
                userUpdate.getLastname(),
                userUpdate.getEmail(),
                userUpdate.getPhone(),
                userUpdate.getAddress(),
                userUpdate.getDNI(),
                userUpdate.getPassword(),
                userUpdate.getState(),
                userUpdate.getDateRegistered(),
                userUpdate.getRol().getNameRol().name()
        );
    }
    public void deleteUser(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario","id",id));
        userToDelete.setState(false);
        userRepository.save(userToDelete);
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
