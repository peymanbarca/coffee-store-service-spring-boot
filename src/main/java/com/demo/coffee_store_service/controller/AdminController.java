package com.demo.coffee_store_service.controller;

import com.demo.coffee_store_service.DTO.Admin.CreateProductDTO;
import com.demo.coffee_store_service.DTO.Admin.MostUsedProducts;
import com.demo.coffee_store_service.DTO.Admin.ProductMenuDTO;
import com.demo.coffee_store_service.DTO.Admin.UpdateProductDTO;
import com.demo.coffee_store_service.DTO.OperationResponse;
import com.demo.coffee_store_service.DTO.exception.GeneralException;
import com.demo.coffee_store_service.domain.Drink;
import com.demo.coffee_store_service.domain.Topping;
import com.demo.coffee_store_service.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/coffeeStoreService")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "Create a New Product (drinks with toppings)", tags = "admin")
    @RequestMapping(value = "/v1/admin/product/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationResponse> createProduct(@RequestBody @Valid CreateProductDTO dto) throws GeneralException {
        return new ResponseEntity<>(adminService.createProduct(dto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Find All Products (drinks)", tags = "admin")
    @RequestMapping(value = "/v1/admin/product/drink/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductMenuDTO<Drink>> getDrinkProducts(@PageableDefault(size = 10) Pageable page) throws GeneralException {
        return new ResponseEntity<>(adminService.getDrinkProducts(page), HttpStatus.OK);
    }

    @ApiOperation(value = "Find All Products (toppings)", tags = "admin")
    @RequestMapping(value = "/v1/admin/product/topping/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductMenuDTO<Topping>> getToppingProducts(@PageableDefault(size = 10) Pageable page) throws GeneralException {
        return new ResponseEntity<>(adminService.getToppingProducts(page), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a Product (drinks with toppings)", tags = "admin")
    @RequestMapping(value = "/v1/admin/product/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationResponse> updateProduct(@RequestBody @Valid UpdateProductDTO dto) throws GeneralException {
        return new ResponseEntity<>(adminService.updateProduct(dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Product (drinks with toppings)", tags = "admin")
    @RequestMapping(value = "/v1/admin/product/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationResponse> deleteProduct(@RequestParam Long id, @RequestParam String type) throws GeneralException {
        return new ResponseEntity<>(adminService.deleteProduct(id , type), HttpStatus.OK);
    }

    @ApiOperation(value = "Find Most Used Products (drinks with toppings)", tags = "admin")
    @RequestMapping(value = "/v1/admin/product/mostUsed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MostUsedProducts>> getMostUsedProducts(@RequestParam String type) throws GeneralException {
        return new ResponseEntity<>(adminService.getMostUsedProducts(type), HttpStatus.OK);
    }

}
