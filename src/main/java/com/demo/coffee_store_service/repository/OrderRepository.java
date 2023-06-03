package com.demo.coffee_store_service.repository;

import com.demo.coffee_store_service.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by zevik on 9/30/22.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
