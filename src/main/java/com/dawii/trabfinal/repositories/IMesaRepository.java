package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByCor(String cor);
    void deleteById(Long id);
}
