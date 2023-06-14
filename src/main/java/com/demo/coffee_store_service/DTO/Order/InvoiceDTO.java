package com.demo.coffee_store_service.DTO.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    @Data
    @AllArgsConstructor
    public static class OrderToppingItemDTO {
        public String toppingName;
        public Double toppingPrice;
    }

    @Data
    @AllArgsConstructor
    public static class OrderItemDTO {
        public String drinkName;
        public Double drinkPrice;
        public List<OrderToppingItemDTO> toppings;

    }

    private List<OrderItemDTO> items;
    private Double originalAmount;
    private Double discountAmount=0D;
    private Double currentAmount;
}
