package com.demo.coffee_store_service;

import com.demo.coffee_store_service.DTO.Order.CreateOrderDTO;
import com.demo.coffee_store_service.DTO.Order.InvoiceDTO;
import com.demo.coffee_store_service.domain.Drink;
import com.demo.coffee_store_service.domain.Topping;
import com.demo.coffee_store_service.repository.DrinkRepository;
import com.demo.coffee_store_service.repository.ToppingRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.demo.coffee_store_service.Utils.CreateRequestUtil.callCreateOrderAPI;
import static com.demo.coffee_store_service.Utils.CreateRequestUtil.castStringAsObject;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoffeeStoreServiceApplication.class})
@AutoConfigureMockMvc
public class CoffeeStoreOrderTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private MockMvc mockMvc;

    private WebApplicationContext webApplicationContext;

    private CoffeeOrderService coffeeOrderService;

    private DrinkRepository drinkRepository;

    private ToppingRepository toppingRepository;

    @Autowired
    public CoffeeStoreOrderTest(CoffeeOrderService coffeeOrderService, DrinkRepository drinkRepository, ToppingRepository toppingRepository,
                                WebApplicationContext webApplicationContext, MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.webApplicationContext = webApplicationContext;
        this.coffeeOrderService = coffeeOrderService;
        this.drinkRepository = drinkRepository;
        this.toppingRepository = toppingRepository;
    }

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

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

        Assert.assertEquals(calculatedDiscount, 0.25 * originalPrice, 0D);


    }


    @Test
    public void testFunctionalityOfCreateOrderAPI() throws Exception {

        log.info(" --------> Running Test : testFunctionalityOfCreateOrderAPI");

        // In order to this test be successful, we need at least some drinks and toppings available in the database.
        // So by consider that at least 3 drinks and toppings are available, this should be run.


        Page<Drink> drinks = drinkRepository.findAll(Pageable.ofSize(3));
        Page<Topping> toppings = toppingRepository.findAll(Pageable.ofSize(3));

        if (drinks.getContent().size() == 3 && toppings.getContent().size() == 3) {

            // create a sample order items using available products (drinks and toppings)
            List<CreateOrderDTO.CreateOrderItemDTO> items = new ArrayList<>();

            items.add(new CreateOrderDTO.CreateOrderItemDTO(drinks.getContent().get(0).getId(),
                    Arrays.asList(
                            new CreateOrderDTO.CreateOrderToppingItemDTO(toppings.getContent().get(0).getId()),
                            new CreateOrderDTO.CreateOrderToppingItemDTO(toppings.getContent().get(1).getId())
                    )));
            items.add(new CreateOrderDTO.CreateOrderItemDTO(drinks.getContent().get(1).getId(),
                    Arrays.asList(
                            new CreateOrderDTO.CreateOrderToppingItemDTO(toppings.getContent().get(0).getId()),
                            new CreateOrderDTO.CreateOrderToppingItemDTO(toppings.getContent().get(1).getId()),
                            new CreateOrderDTO.CreateOrderToppingItemDTO(toppings.getContent().get(2).getId())
                    )));
            items.add(new CreateOrderDTO.CreateOrderItemDTO(drinks.getContent().get(2).getId(),
                    Arrays.asList(
                            new CreateOrderDTO.CreateOrderToppingItemDTO(toppings.getContent().get(0).getId()),
                            new CreateOrderDTO.CreateOrderToppingItemDTO(toppings.getContent().get(2).getId())
                    )));

            CreateOrderDTO dto = new CreateOrderDTO(items);


            MvcResult mvcResult = callCreateOrderAPI(dto, new HttpHeaders(), mockMvc);
            int status = mvcResult.getResponse().getStatus();
            Assert.assertEquals(HttpStatus.OK.value(), status);

            // Evaluate Expectations (for original amount)
            final Double[] originalPriceExpected = {0D};
            items.forEach(item -> {
                originalPriceExpected[0] += drinkRepository.findById(item.getDrinkId()).get().getPrice();
                item.getToppings().forEach(topping -> {
                    originalPriceExpected[0] += toppingRepository.findById(topping.getToppingId()).get().getPrice();
                });
            });

            String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            InvoiceDTO invoice = (InvoiceDTO) castStringAsObject(responseBody, InvoiceDTO.class);
            Assert.assertEquals(originalPriceExpected[0], invoice.getOriginalAmount());


            log.info("Called Create Order API: Response Body = " + invoice.toString());
            log.info("------------------------------------");
        }
        else {
            log.info(" --------> There is not enough data (drinks and toppings) available to perform this test, " +
                    " At least 3 drinks and toppings should be existed in database.");
        }



    }
}
