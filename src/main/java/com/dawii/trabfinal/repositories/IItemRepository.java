package com.dawii.trabfinal.repositories;

import com.dawii.trabfinal.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemRepository extends JpaRepository<Item, Long> {
}
