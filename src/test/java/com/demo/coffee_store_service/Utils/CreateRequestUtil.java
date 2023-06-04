package com.demo.coffee_store_service.Utils;



import com.demo.coffee_store_service.DTO.Admin.CreateProductDTO;
import com.demo.coffee_store_service.DTO.Admin.UpdateProductDTO;
import com.demo.coffee_store_service.DTO.Order.CreateOrderDTO;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


/**
 * Created by zevik on 9/1/20.
 */
public class CreateRequestUtil {


    public static MvcResult callCreateProductAPI(CreateProductDTO body, HttpHeaders headers, MockMvc mockMvc) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(body);

        return mockMvc.perform(post("/coffeeStoreService/v1/admin/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .headers(headers)
                .contentType(APPLICATION_JSON))
                .andReturn();
    }

    public static MvcResult callUpdateProductAPI(UpdateProductDTO body, HttpHeaders headers, MockMvc mockMvc) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(body);

        return mockMvc.perform(put("/coffeeStoreService/v1/admin/product/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .headers(headers)
                .contentType(APPLICATION_JSON))
                .andReturn();
    }

    public static MvcResult callDeleteProductAPI(Long id, String type, HttpHeaders headers, MockMvc mockMvc) throws Exception {

        return mockMvc.perform(delete("/coffeeStoreService/v1/admin/product/delete"
                    .concat("?").concat("id=".concat(id.toString()).concat("&type=").concat(type)))
                .headers(headers)
                .contentType(APPLICATION_JSON))
                .andReturn();
    }

    public static MvcResult callGetProductsAPI(String type, HttpHeaders headers, MockMvc mockMvc) throws Exception {

        if (type.equals(CreateProductDTO.PRODUCT_TYPE.TOPPING.name())) {
            return mockMvc.perform(get("/coffeeStoreService/v1/admin/product/topping/all")
                    .headers(headers)
                    .contentType(APPLICATION_JSON))
                    .andReturn();
        }
        else {
            return mockMvc.perform(get("/coffeeStoreService/v1/admin/product/drink/all")
                    .headers(headers)
                    .contentType(APPLICATION_JSON))
                    .andReturn();
        }

    }

    public static MvcResult callCreateOrderAPI(CreateOrderDTO body, HttpHeaders headers, MockMvc mockMvc) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(body);

        return mockMvc.perform(post("/coffeeStoreService/v1/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .headers(headers)
                .contentType(APPLICATION_JSON))
                .andReturn();


    }

    public static Object castStringAsObject(String jsonString, Class object){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, object);
    }

}
