package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.state = ?1")
    List<User> findByState(Boolean state);
    //buscar usuario por su email
    Optional<User> findByEmail(String email);

    Optional<User> findByDNI(String dni);

}
