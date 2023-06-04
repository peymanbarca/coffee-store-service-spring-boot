package com.demo.coffee_store_service.service;

import com.demo.coffee_store_service.DTO.Admin.CreateProductDTO;
import com.demo.coffee_store_service.DTO.Admin.MostUsedProducts;
import com.demo.coffee_store_service.DTO.Admin.ProductMenuDTO;
import com.demo.coffee_store_service.DTO.Admin.UpdateProductDTO;
import com.demo.coffee_store_service.DTO.OperationResponse;
import com.demo.coffee_store_service.DTO.exception.GeneralException;
import com.demo.coffee_store_service.domain.Drink;
import com.demo.coffee_store_service.domain.Topping;
import com.demo.coffee_store_service.repository.DrinkRepository;
import com.demo.coffee_store_service.repository.OrderItemsRepository;
import com.demo.coffee_store_service.repository.ToppingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by zevik on 6/1/23.
 */
@Service
public class AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final MessageService messageService;

    private final DrinkRepository drinkRepository;

    private final ToppingRepository toppingRepository;

    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public AdminService(MessageService messageService, DrinkRepository drinkRepository,
                        ToppingRepository toppingRepository, OrderItemsRepository orderItemsRepository) {
        this.messageService = messageService;
        this.drinkRepository = drinkRepository;
        this.toppingRepository = toppingRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    public OperationResponse createProduct(CreateProductDTO dto) throws GeneralException {

        if (dto.getType().equals(CreateProductDTO.PRODUCT_TYPE.DRINK)) {
            Optional<Drink> possibleExistedDrink = drinkRepository.findOneByName(dto.getName());
            if (possibleExistedDrink.isPresent()) {
                log.error("Tried to create new drink product with existing name");
                throw new GeneralException(OperationResponse.ResponseStatusEnum.BAD_REQUEST,
                        messageService.getMessage("error.request.drink.existed"));
            }


            Drink drink = new Drink().builder().name(dto.getName()).price(dto.getPrice()).build();
            drinkRepository.save(drink);
            return new OperationResponse(OperationResponse.ResponseStatusEnum.SUCCESS,
                    messageService.getMessage("msg.product.created"));
        }
        else if (dto.getType().equals(CreateProductDTO.PRODUCT_TYPE.TOPPING)) {
            Optional<Topping> possibleExistedTopping = toppingRepository.findOneByName(dto.getName());
            if (possibleExistedTopping.isPresent()) {
                log.error("Tried to create new topping product with existing name");
                throw new GeneralException(OperationResponse.ResponseStatusEnum.BAD_REQUEST,
                        messageService.getMessage("error.request.topping.existed"));
            }


            Topping topping = new Topping().builder().name(dto.getName()).price(dto.getPrice()).build();
            toppingRepository.save(topping);
            return new OperationResponse(OperationResponse.ResponseStatusEnum.SUCCESS,
                    messageService.getMessage("msg.product.created"));
        }
        else {
            log.error(String.format("Tried to create new product with false type: %s", dto.getType()));
            throw new GeneralException(OperationResponse.ResponseStatusEnum.BAD_REQUEST,
                    messageService.getMessage("error.request.param").concat(" , Type is wrong."));
        }
    }

    public ProductMenuDTO<Drink> getDrinkProducts(Pageable page) {

        Page<Drink> drinks = drinkRepository.findAll(page);
        return new ProductMenuDTO<Drink>(drinks.getContent(), CreateProductDTO.PRODUCT_TYPE.DRINK,
                drinks.getTotalElements(), drinks.getPageable().getPageNumber(), drinks.getSize() );
    }

    public ProductMenuDTO<Topping> getToppingProducts(Pageable page) {

        Page<Topping> toppings = toppingRepository.findAll(page);
        return new ProductMenuDTO<Topping>(toppings.getContent(), CreateProductDTO.PRODUCT_TYPE.TOPPING,
                toppings.getTotalElements(), toppings.getPageable().getPageNumber(), toppings.getSize() );
    }

    public OperationResponse updateProduct(UpdateProductDTO dto) throws GeneralException {

        if (dto.getType().equals(CreateProductDTO.PRODUCT_TYPE.DRINK)) {
            Optional<Drink> possibleExistedDrink = drinkRepository.findById(dto.getId());
            if (!possibleExistedDrink.isPresent()) {
                log.error("Tried to update drink product that not existed");
                throw new GeneralException(OperationResponse.ResponseStatusEnum.NOT_FOUND,
                        messageService.getMessage("error.request.drink.notFound"));
            }


            Drink targetDrink = possibleExistedDrink.get();
            targetDrink.setName(dto.getName());
            targetDrink.setPrice(dto.getPrice());
            drinkRepository.save(targetDrink);
            return new OperationResponse(OperationResponse.ResponseStatusEnum.SUCCESS,
                    messageService.getMessage("msg.product.updated"));
        }
        else if (dto.getType().equals(CreateProductDTO.PRODUCT_TYPE.TOPPING)) {
            Optional<Topping> possibleExistedTopping = toppingRepository.findById(dto.getId());
            if (!possibleExistedTopping.isPresent()) {
                log.error("Tried to update topping product that not existed");
                throw new GeneralException(OperationResponse.ResponseStatusEnum.NOT_FOUND,
                        messageService.getMessage("error.request.topping.notFound"));
            }


            Topping targetTopping = possibleExistedTopping.get();
            targetTopping.setName(dto.getName());
            targetTopping.setPrice(dto.getPrice());
            toppingRepository.save(targetTopping);
            return new OperationResponse(OperationResponse.ResponseStatusEnum.SUCCESS,
                    messageService.getMessage("msg.product.updated"));
        }
        else {
            log.error(String.format("Tried to update product with false type: %s", dto.getType()));
            throw new GeneralException(OperationResponse.ResponseStatusEnum.BAD_REQUEST,
                    messageService.getMessage("error.request.param").concat(" , Type is wrong."));
        }

    }

    public OperationResponse deleteProduct(Long id, String type) throws GeneralException {

        if (type.equalsIgnoreCase(CreateProductDTO.PRODUCT_TYPE.DRINK.name())) {
            Optional<Drink> possibleExistedDrink = drinkRepository.findById(id);
            if (!possibleExistedDrink.isPresent()) {
                log.error("Tried to delete drink product that not existed");
                throw new GeneralException(OperationResponse.ResponseStatusEnum.NOT_FOUND,
                        messageService.getMessage("error.request.drink.notFound"));
            }


            drinkRepository.deleteById(id);
            return new OperationResponse(OperationResponse.ResponseStatusEnum.SUCCESS,
                    messageService.getMessage("msg.product.deleted")
            );
        }
        else if (type.equalsIgnoreCase(CreateProductDTO.PRODUCT_TYPE.TOPPING.name())) {
            Optional<Topping> possibleExistedTopping = toppingRepository.findById(id);
            if (!possibleExistedTopping.isPresent()) {
                log.error("Tried to delete topping product that not existed");
                throw new GeneralException(OperationResponse.ResponseStatusEnum.NOT_FOUND,
                        messageService.getMessage("error.request.topping.notFound"));
            }


            toppingRepository.deleteById(id);
            return new OperationResponse(OperationResponse.ResponseStatusEnum.SUCCESS,
                    messageService.getMessage("msg.product.deleted"));
        }
        else {
            log.error(String.format("Tried to delete product with false type: %s", type));
            throw new GeneralException(OperationResponse.ResponseStatusEnum.BAD_REQUEST,
                    messageService.getMessage("error.request.param").concat(" , Type is wrong."));
        }

    }

    public List<MostUsedProducts> getMostUsedProducts(String type) throws GeneralException {
        if (type.equalsIgnoreCase(CreateProductDTO.PRODUCT_TYPE.DRINK.name())) {
            return orderItemsRepository.findMostUsedDrinks();
        }
        else if (type.equalsIgnoreCase(CreateProductDTO.PRODUCT_TYPE.TOPPING.name())) {
            return orderItemsRepository.findMostUsedToppings();
        }
        else {
            log.error(String.format("Tried to get most used products with false type: %s", type));
            throw new GeneralException(OperationResponse.ResponseStatusEnum.BAD_REQUEST,
                    messageService.getMessage("error.request.param").concat(" , Type is wrong."));
        }


    }
}
