package com.demo.coffee_store_service.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMenuDTO<T> {

    private List<T> items;
    private CreateProductDTO.PRODUCT_TYPE type;
    private long totalNumber;
    private Integer pageNumber;
    private Integer pageSize;
}
