package com.demo.coffee_store_service;



import com.demo.coffee_store_service.DTO.Admin.CreateProductDTO;
import com.demo.coffee_store_service.DTO.Admin.ProductMenuDTO;
import com.demo.coffee_store_service.DTO.Admin.UpdateProductDTO;
import com.demo.coffee_store_service.controller.Filter.SecretKeyFilter;
import com.demo.coffee_store_service.domain.Drink;
import com.demo.coffee_store_service.repository.DrinkRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static com.demo.coffee_store_service.Utils.CreateRequestUtil.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoffeeStoreServiceApplication.class})
@AutoConfigureMockMvc
public class CoffeeStoreAdminTest {


    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Autowired private DrinkRepository drinkRepository;

    @Test
    public void testAdminAuthorization() throws Exception {

        log.info(" --------> Running Test : testAdminAuthorization");

        // Test Create new product (Drink) without providing secret key
        CreateProductDTO dto = new CreateProductDTO("Espresso", 5.5D, CreateProductDTO.PRODUCT_TYPE.DRINK);
        MvcResult mvcResult = callCreateProductAPI(dto, new HttpHeaders(), mockMvc);
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);

    }


    @Test
    public void testAdminFunctionalityOfNewProduct() throws Exception {

        log.info(" --------> Running Test : testAdminFunctionalityOfNewProduct");

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecretKeyFilter.SECRET_KEY_HEADER_NAME, SecretKeyFilter.SECRET_KEY_HEADER_VALUE);

        // Test Create new product (Drink) with new random name
        String ranodmProductName = UUID.randomUUID().toString().substring(10);
        CreateProductDTO dto = new CreateProductDTO(ranodmProductName, 5.5D, CreateProductDTO.PRODUCT_TYPE.DRINK);
        MvcResult mvcResult = callCreateProductAPI(dto, headers, mockMvc);
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.CREATED.value(), status);

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        log.info("Called Create API: Response Body = " + responseBody);
        log.info("------------------------------------");

        // Test Create new product with existing name (Drink)
        mvcResult = callCreateProductAPI(dto, headers, mockMvc);
        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), status);

        responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        log.info("Called Create API With Existing Name: Response Body = " + responseBody);
        log.info("------------------------------------");

        // Test Update of the same product (Drink)
        Optional<Drink> drinkOptional = drinkRepository.findOneByName(ranodmProductName);
        Assert.assertTrue(drinkOptional.isPresent());
        assert drinkOptional.isPresent();
        Long drinkId = drinkOptional.get().getId();
        Double newPrice = 5.8D;
        UpdateProductDTO updateProductDTO = new UpdateProductDTO(ranodmProductName, newPrice, dto.getType(), drinkId);

        mvcResult = callUpdateProductAPI(updateProductDTO, headers, mockMvc);
        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.OK.value(), status);

        drinkOptional = drinkRepository.findOneByName(ranodmProductName);
        Assert.assertTrue(drinkOptional.isPresent());
        assert drinkOptional.isPresent();
        Assert.assertEquals(newPrice, drinkOptional.get().getPrice());

        responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        log.info("Called Update API: Response Body = " + responseBody);
        log.info("------------------------------------");


        // Test Delete of the same product (Drink)
        mvcResult = callDeleteProductAPI(drinkId, CreateProductDTO.PRODUCT_TYPE.DRINK.toString(), headers, mockMvc);
        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.OK.value(), status);

        responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        log.info("Called Delete API: Response Body = " + responseBody);
        log.info("------------------------------------");


    }


    @Test
    public void testAdminFunctionalityOfFetchProducts() throws Exception {

        log.info(" --------> Running Test : testAdminFunctionalityOfFetchProducts");

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecretKeyFilter.SECRET_KEY_HEADER_NAME, SecretKeyFilter.SECRET_KEY_HEADER_VALUE);

        // ----- Test get drink products
        MvcResult mvcResult = callGetProductsAPI(CreateProductDTO.PRODUCT_TYPE.DRINK.name(), headers, mockMvc);
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.OK.value(), status);

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ProductMenuDTO menu = (ProductMenuDTO) castStringAsObject(responseBody, ProductMenuDTO.class);
        Assert.assertEquals(CreateProductDTO.PRODUCT_TYPE.DRINK, menu.getType());
        log.info("Called Get Products (Drink) API: Response Body = " + menu);
        log.info("------------------------------------");


        // ----- Test get topping products
        mvcResult = callGetProductsAPI(CreateProductDTO.PRODUCT_TYPE.TOPPING.name(), headers, mockMvc);
        status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(HttpStatus.OK.value(), status);

        responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        menu = (ProductMenuDTO) castStringAsObject(responseBody, ProductMenuDTO.class);
        Assert.assertEquals(CreateProductDTO.PRODUCT_TYPE.TOPPING, menu.getType());

        log.info("Called Get Products (Topping) API: Response Body = " + menu);
        log.info("------------------------------------");

    }




}
