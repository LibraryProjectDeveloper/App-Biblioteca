package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //listar usuarios por su estado
    List<User> findByState(Boolean state);

    //buscar usuario por su id
    Optional<User> findByCode(Long id);

    //buscar usuario por su email
    Optional<User> findByEmail(String email);

    void deleteByCode(Long id);
}
