package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProdutoRepository extends JpaRepository<Produto, Long> {
}
