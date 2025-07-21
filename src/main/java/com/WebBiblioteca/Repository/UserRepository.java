package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.Role;
import com.WebBiblioteca.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.state = ?1")
    Page<User> findByState(Boolean state, Pageable pageable);

    //buscar usuario por su email
    Optional<User> findByEmail(String email);
    Optional<User> findByDNI(String dni);


    Optional<User> findByNameContainingIgnoreCase(String name);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR u.email = ?1 OR u.DNI = ?1")
    Page<User> findByNameOrEmailOrDNI(String query,Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.DNI = ?1 AND u.rol.nameRol = ?2")
    Optional<User> findByDNIAndRol(String dni, Role role);

    @Query("SELECT u FROM User u JOIN u.rol r WHERE r.idRol = ?1")
    Page<User> findByRol(Long idRol,Pageable pageable);

}
