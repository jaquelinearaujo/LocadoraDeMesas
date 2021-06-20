package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IHistoricoRepository extends JpaRepository<Historico, Long> {
    List<Optional<Historico>> findByCodLocacao(Long codLocacao);
}
