package com.demo.coffee_store_service.DTO.exception;

import com.demo.coffee_store_service.DTO.OperationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralException extends Exception {
    private OperationResponse.ResponseStatusEnum status= OperationResponse.ResponseStatusEnum.INTERNAL_SERVER_ERROR;
    private String message;
}
