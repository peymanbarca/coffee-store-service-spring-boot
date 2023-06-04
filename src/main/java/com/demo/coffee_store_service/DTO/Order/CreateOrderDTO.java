package com.demo.coffee_store_service.DTO.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zevik on 6/1/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOrderToppingItemDTO {
        @NotNull public Long toppingId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOrderItemDTO {
        @NotNull public Long drinkId;
        public List<CreateOrderToppingItemDTO> toppings;

    }

    @NotNull private List<CreateOrderItemDTO> items;
}
