package com.demo.coffee_store_service.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by zevik on 6/1/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {

    @NotNull(message = "name must be provided.") private String name;
    @NotNull(message = "price must be provided.") private Double price;
    @NotNull(message = "type must be provided.") private PRODUCT_TYPE type;

    public static enum PRODUCT_TYPE {
        DRINK, TOPPING
    }
}
