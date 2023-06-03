package com.demo.coffee_store_service;

import com.demo.coffee_store_service.DTO.Order.InvoiceDTO;
import com.demo.coffee_store_service.service.CoffeeOrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by zevik on 6/3/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoffeeStoreServiceApplication.class})
@AutoConfigureMockMvc
public class CoffeeStoreOrderTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @Test
    public void testDiscountLogic1() {

        log.info(" --------> Running Test : testDiscountLogic, Scenario 1");

        List<InvoiceDTO.OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new InvoiceDTO.OrderItemDTO("Black Coffee", 4D,
                Arrays.asList(
                        new InvoiceDTO.OrderToppingItemDTO("Milk", 2D),
                        new InvoiceDTO.OrderToppingItemDTO("Hazelnut syrup", 3D))
        ));
        orderItems.add(new InvoiceDTO.OrderItemDTO("Latte", 5D,
                Arrays.asList(
                        new InvoiceDTO.OrderToppingItemDTO("Milk", 2D),
                        new InvoiceDTO.OrderToppingItemDTO("Chocolate sauce", 5D))
        ));
        double originalPrice = 21;

        double calculatedDiscount = coffeeOrderService.findDiscountBasedOnOrderItems(
                orderItems, originalPrice);

        Assert.assertEquals(calculatedDiscount, 0.25* originalPrice, 0D);


    }

    @Test
    public void testDiscountLogic2() {

        log.info(" --------> Running Test : testDiscountLogic, Scenario 2");

        List<InvoiceDTO.OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new InvoiceDTO.OrderItemDTO("Black Coffee", 4D,
                Arrays.asList(
                        new InvoiceDTO.OrderToppingItemDTO("Milk", 2D),
                        new InvoiceDTO.OrderToppingItemDTO("Hazelnut syrup", 3D))
        ));

        double originalPrice = 9;

        double calculatedDiscount = coffeeOrderService.findDiscountBasedOnOrderItems(
                orderItems, originalPrice);

        Assert.assertEquals(calculatedDiscount, 0, 0D);


    }

    @Test
    public void testDiscountLogic3() {

        log.info(" --------> Running Test : testDiscountLogic, Scenario 3");

        List<InvoiceDTO.OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new InvoiceDTO.OrderItemDTO("Black Coffee", 4D,
                Arrays.asList(
                        new InvoiceDTO.OrderToppingItemDTO("Milk", 2D),
                        new InvoiceDTO.OrderToppingItemDTO("Hazelnut syrup", 3D),
                        new InvoiceDTO.OrderToppingItemDTO("Lemon", 2D)
                )
                )
        );
        orderItems.add(new InvoiceDTO.OrderItemDTO("Latte", 5D,
                Arrays.asList(
                        new InvoiceDTO.OrderToppingItemDTO("Milk", 2D),
                        new InvoiceDTO.OrderToppingItemDTO("Chocolate sauce", 5D))));
        orderItems.add(new InvoiceDTO.OrderItemDTO("Mocha", 6D,
                Collections.singletonList(
                        new InvoiceDTO.OrderToppingItemDTO("Milk", 2D))
        ));
        orderItems.add(new InvoiceDTO.OrderItemDTO("Tea", 3D,
                Collections.emptyList()
        ));

        double originalPrice = 34;

        double calculatedDiscount = coffeeOrderService.findDiscountBasedOnOrderItems(
                orderItems, originalPrice);

        Assert.assertEquals(calculatedDiscount, 3, 0D);


    }


    @Test
    public void testFunctionalityOfPlaceOrderAPI() throws Exception {}
}
