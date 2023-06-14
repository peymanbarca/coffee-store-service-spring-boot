package com.demo.coffee_store_service.repository;

import com.demo.coffee_store_service.domain.Drink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {
    Optional<Drink> findOneByName(String name);
}
