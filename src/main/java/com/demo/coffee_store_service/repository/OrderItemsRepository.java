package com.demo.coffee_store_service.repository;

import com.demo.coffee_store_service.DTO.Admin.MostUsedProducts;
import com.demo.coffee_store_service.domain.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zevik on 9/30/22.
 */
@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    @Query(value = "select new com.demo.coffee_store_service.DTO.Admin.MostUsedProducts(count(oi), oi.topping.name)" +
            "  from OrderItems oi  " +
            " group by oi.topping.name order by count(oi) desc")
    List<MostUsedProducts> findMostUsedToppings();

    @Query(value = "select new com.demo.coffee_store_service.DTO.Admin.MostUsedProducts(count(distinct oi.order.id), " +
            " oi.drink.name)" +
            "  from OrderItems oi  " +
            " group by oi.drink.name order by count(oi.order.id) desc")
    List<MostUsedProducts> findMostUsedDrinks();
}
