package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILocacaoRepository extends JpaRepository<Locacao, Long> {
}
