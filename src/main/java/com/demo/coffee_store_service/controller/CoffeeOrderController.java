package com.demo.coffee_store_service.controller;


import com.demo.coffee_store_service.DTO.Order.CreateOrderDTO;
import com.demo.coffee_store_service.DTO.Order.InvoiceDTO;
import com.demo.coffee_store_service.DTO.exception.GeneralException;
import com.demo.coffee_store_service.service.CoffeeOrderService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;


/**
 * Created by zevik on 9/30/22.
 */
@Controller
@RequestMapping("/coffeeStoreService")
public class CoffeeOrderController {


    private final CoffeeOrderService coffeeOrderService;

    @Autowired
    public CoffeeOrderController(CoffeeOrderService coffeeOrderService) {
        this.coffeeOrderService = coffeeOrderService;
    }

    @ApiOperation(value = "Create New Order (drinks with toppings)", tags = "users")
    @RequestMapping(value = "/v1/order/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InvoiceDTO> createOrder(@RequestBody CreateOrderDTO dto) throws GeneralException {
        return new ResponseEntity<>(coffeeOrderService.createOrder(dto), HttpStatus.OK);
    }



}
