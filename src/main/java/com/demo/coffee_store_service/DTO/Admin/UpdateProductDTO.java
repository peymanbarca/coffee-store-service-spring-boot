
package com.demo.coffee_store_service.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDTO extends CreateProductDTO {

    @NotNull(message = "id of the product (Drink/Topping) must be provided.") private Long id;

    public UpdateProductDTO(String name, Double price, PRODUCT_TYPE type, Long id) {
        super(name, price, type);
        this.id = id;
    }
}
