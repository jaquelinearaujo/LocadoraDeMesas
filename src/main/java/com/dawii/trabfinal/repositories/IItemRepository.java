package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IItemRepository extends JpaRepository<Item, Long> {
    List<Optional<Item>> findByCodLocacao(Long codLocacao);
}
