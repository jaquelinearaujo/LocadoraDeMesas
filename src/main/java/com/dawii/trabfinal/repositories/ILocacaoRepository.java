package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ILocacaoRepository extends JpaRepository<Locacao, Long> {
    List<Optional<Locacao>> findByCodMesa(Long codMesa);
    void deleteById(Long id);
}
