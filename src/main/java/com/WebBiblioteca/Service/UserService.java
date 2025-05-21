package com.WebBiblioteca.Service;

import com.WebBiblioteca.DTO.Usuario.UsuarioRequest;
import com.WebBiblioteca.DTO.Usuario.UsuarioResponse;
import com.WebBiblioteca.Model.Rol;
import com.WebBiblioteca.Model.User;
import com.WebBiblioteca.Repository.RolRepository;
import com.WebBiblioteca.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> getAllUsers() {
        List<UsuarioResponse> usuarioResponseList =  userRepository.findAll().
                stream().map(user -> new UsuarioResponse(
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

        if (usuarioResponseList.isEmpty()) {
            throw new RuntimeException("No hay usuarios disponibles");
        }

        return usuarioResponseList;
    }

    @Transactional
    public void addUser(UsuarioRequest user) {
        Rol rol = rolRepository.findByIdRol(user.getIdRol()).orElse(null);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario ya está registrado");
        }

        if (rol == null) {
            throw new RuntimeException("El rol no existe");
        }

        if (userRepository.findByDNI(user.getDNI()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setLastname(user.getLastname());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setAddress(user.getAddress());
        newUser.setDNI(user.getDNI());
        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        newUser.setState(true);
        newUser.setDateRegistered(LocalDateTime.now());
        newUser.setRol(rol);
        userRepository.save(newUser);
    }

    public void updateUser(UsuarioRequest user) {
        User userToUpdate = userRepository.findById(user.getIdUsuario()).orElse(null);
        if (userToUpdate == null) {
            throw new RuntimeException("El usuario no existe");
        }

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
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getState() != null) {
            userToUpdate.setState(user.getState());
        }
        if (user.getIdRol() != null) {
            Rol rol = rolRepository.findByIdRol(user.getIdRol()).orElse(null);
            if (rol == null) {
                throw new RuntimeException("El rol no existe");
            }
            userToUpdate.setRol(rol);
        }

        userRepository.save(userToUpdate);
    }


    /*
    public User loginUser(String email, String password ){
        User user = userRepository.getUserByEmail(email);
        if(user != null && user.getPassword().equals(password) && user.getRole().getNameRol() == Role.LIBRARIAN && user.getState()){
            return user;
        }
        return null;
    }
    public boolean deleteUser(Long id){
        if(getAllUsers().get(id) !=null){
            userRepository.deleteUser(id);
            return true;
        }else{
            return false;
        }
    }
    public User updateUser(User user,Long id){
        User existingUser = userRepository.getUserById(id);
        if ( existingUser != null){
            if(user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if(user.getLastname() != null) {
                existingUser.setLastname(user.getLastname());
            }
            if(user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if(user.getPhone() != null) {
                existingUser.setPhone(user.getPhone());
            }
            if(user.getAddress() != null) {
                existingUser.setAddress(user.getAddress());
            }
            if(user.getDNI() != null) {
                existingUser.setDNI(user.getDNI());
            }
            if(user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            }
            if(user.getState() != null) {
                existingUser.setState(user.getState());
            }
            if(user.getRole() != null) {
                existingUser.setRole(user.getRole());
            }
            if(user.getDateRegistered() != null) {
                existingUser.setDateRegistered(user.getDateRegistered());
            }
            return userRepository.updateUser(id,existingUser);
        }else{
            return null;
        }
    }
     */
}
