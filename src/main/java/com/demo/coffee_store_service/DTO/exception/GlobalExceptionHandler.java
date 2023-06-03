package com.demo.coffee_store_service.DTO.exception;

import com.demo.coffee_store_service.DTO.OperationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by zevik on 10/30/20.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<OperationResponse> handlyMyCustomException(GeneralException e) {
        LOGGER.error("error occurred {}", e);
        return new ResponseEntity<OperationResponse>(new OperationResponse(e.getStatus(),e.getMessage()),e.getStatus().getHttpStatus());
    }

}
