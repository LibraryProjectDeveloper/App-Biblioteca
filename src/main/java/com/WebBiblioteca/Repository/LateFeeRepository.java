package com.WebBiblioteca.Repository;

import com.WebBiblioteca.Model.LateFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LateFeeRepository extends JpaRepository<LateFee,Long> {
}
