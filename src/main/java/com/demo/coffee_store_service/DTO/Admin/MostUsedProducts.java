package com.demo.coffee_store_service.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zevik on 6/3/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostUsedProducts {

    private Long count;
    private String name;
}
