package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILocacaoRepository extends JpaRepository<Locacao, Long> {
    List<Locacao> findByCodPessoa(Long usuario);
}
