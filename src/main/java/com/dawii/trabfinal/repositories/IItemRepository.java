package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCodigoLocacao(Long codigoLocacao);
}
