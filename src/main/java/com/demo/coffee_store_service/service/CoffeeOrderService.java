package com.demo.coffee_store_service.service;

import com.demo.coffee_store_service.DTO.Order.CreateOrderDTO;
import com.demo.coffee_store_service.DTO.Order.InvoiceDTO;
import com.demo.coffee_store_service.domain.Drink;
import com.demo.coffee_store_service.domain.Order;
import com.demo.coffee_store_service.domain.OrderItems;
import com.demo.coffee_store_service.domain.Topping;
import com.demo.coffee_store_service.repository.DrinkRepository;
import com.demo.coffee_store_service.repository.OrderItemsRepository;
import com.demo.coffee_store_service.repository.OrderRepository;
import com.demo.coffee_store_service.repository.ToppingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by zevik on 9/30/22.
 */
@Service
public class CoffeeOrderService {

    private final Logger log = LoggerFactory.getLogger(CoffeeOrderService.class);

    private final MessageService messageService;

    private final DrinkRepository drinkRepository;

    private final ToppingRepository toppingRepository;

    private final OrderRepository orderRepository;

    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public CoffeeOrderService(MessageService messageService, DrinkRepository drinkRepository,
                              ToppingRepository toppingRepository, OrderRepository orderRepository,
                              OrderItemsRepository orderItemsRepository) {
        this.messageService = messageService;
        this.drinkRepository = drinkRepository;
        this.toppingRepository = toppingRepository;
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    public InvoiceDTO createOrder(CreateOrderDTO dto) {


        List<InvoiceDTO.OrderItemDTO> orderItems = new ArrayList<>();
        Map<Drink, List<Topping>> orderItemsToBePersistedOnDB = new HashMap<>();
        final Double[] originalPrice = {0D};
        Double discountAmount = 0D;

        // Iterate over items of the given order, to find the matched drinks and their corresponding toppings,
        // as well as prices
        // Note that, when a drink or topping can't be found by given Id, we skip it and write an error log
        dto.getItems().forEach(item -> {
            Optional<Drink> possibleExistedDrink = drinkRepository.findById(item.getDrinkId());
            if (possibleExistedDrink.isPresent()) {
                Drink targetDrink = possibleExistedDrink.get();
                List<Topping> desiredToppings = new ArrayList<>();


                List<InvoiceDTO.OrderToppingItemDTO> orderToppingItemDTOS = new ArrayList<>();
                originalPrice[0] += targetDrink.getPrice();

                item.getToppings().forEach(topping -> {
                    Optional<Topping> possibleExistedTopping = toppingRepository.findById(topping.getToppingId());
                    if (possibleExistedTopping.isPresent()) {
                        Topping targetTopping = possibleExistedTopping.get();
                        desiredToppings.add(targetTopping);
                        orderToppingItemDTOS.add(new InvoiceDTO.OrderToppingItemDTO(
                                targetTopping.getName(), targetTopping.getPrice()));
                        originalPrice[0] += targetTopping.getPrice();
                    }
                    else
                        log.error(String.format("The Topping with Id %s given in the order items," +
                                " where not found in the product list", topping.getToppingId()));

                });
                orderItems.add(new InvoiceDTO.OrderItemDTO(
                        targetDrink.getName(), targetDrink.getPrice(), orderToppingItemDTOS));
                orderItemsToBePersistedOnDB.put(targetDrink, desiredToppings);
            }
            else
                log.error(String.format("The Drink with Id %s given in the order items," +
                                " where not found in the product list", item.getDrinkId()));
        });

        // find the discount and current amount (after discount) of the invoice
        discountAmount = findDiscountBasedOnOrderItems(orderItems, originalPrice[0]);
        Double currentAmount = originalPrice[0] - discountAmount;

        // save a new order (along with its items: Drinks and Their Toppings) on DB
        Order newOrder = new Order().builder().createDate(Timestamp.valueOf(LocalDateTime.now()))
                .originalAmount(originalPrice[0]).discountAmount(discountAmount).build();
        newOrder = orderRepository.save(newOrder);
        final Order finalNewOrder = newOrder;

        orderItemsToBePersistedOnDB.entrySet().forEach(itemToBePersist->{
            Drink targetDrink = itemToBePersist.getKey();
            List<Topping> desiredToppings = itemToBePersist.getValue();

            desiredToppings.forEach(topping -> {
                OrderItems item = new OrderItems().builder().drink(targetDrink).topping(topping).order(finalNewOrder)
                        .build();
                orderItemsRepository.save(item);
            });
        });




        return new InvoiceDTO(orderItems, originalPrice[0], discountAmount, currentAmount);
    }


    public Double findDiscountBasedOnOrderItems(List<InvoiceDTO.OrderItemDTO> orderItems, double originalPrice) {

        double discount = 0D;
        double promotion1 = 0D;
        double promotion2 = 0D;

        if (originalPrice > 12)
            promotion1 = 0.25 * originalPrice;

        // If there are 3 or more drinks in the cart, the one with the lowest amount (including toppings)
        // should be free.
        if (orderItems.size() >= 3) {
            List<Double> orderItemsPrice = new ArrayList<>();
            orderItems.forEach(item -> {
                // Find the price of the item (including the drink and all of its toppings)
                double itemPrice = item.getDrinkPrice() + item.getToppings().stream()
                        .map(InvoiceDTO.OrderToppingItemDTO::getToppingPrice).mapToDouble(Double::doubleValue).sum();
                orderItemsPrice.add(itemPrice);
            });
            promotion2 =  Collections.min(orderItemsPrice);
        }

        // If the cart is eligible for both promotions, the promotion with the lowest cart amount should be
        // used and the other one should be ignored.
        if (promotion1 > 0 && promotion2 > 0)
            discount = Double.min(promotion1, promotion2);
        else
            discount = Double.max(promotion1, promotion2);

        return discount;

    }

}
