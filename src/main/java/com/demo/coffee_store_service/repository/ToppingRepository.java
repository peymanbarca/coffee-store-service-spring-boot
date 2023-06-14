package com.demo.coffee_store_service.repository;

import com.demo.coffee_store_service.domain.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, Long> {
    Optional<Topping> findOneByName(String name);

}
