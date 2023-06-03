/**
 * This is the common structure for all responses
 * if the response contains a list(array) then it will have 'items' field
 * if the response contains a single item then it will have 'item'  field
 */


package com.demo.coffee_store_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationResponse {
    public enum ResponseStatusEnum {

        SUCCESS(HttpStatus.OK), ERROR(HttpStatus.INTERNAL_SERVER_ERROR),MOVED_PERMANENTLY(HttpStatus.MOVED_PERMANENTLY),
        NO_ACCESS(HttpStatus.FORBIDDEN), BAD_REQUEST(HttpStatus.BAD_REQUEST),TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS),
        NOT_FOUND(HttpStatus.NOT_FOUND),INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
        METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
        SERVICE_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE), REMOTE_SERVICE_ERROR(HttpStatus.FAILED_DEPENDENCY),
        CONFLICT_DATA(HttpStatus.CONFLICT),UPGRADE_REQUIRED(HttpStatus.UPGRADE_REQUIRED);

        ResponseStatusEnum(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }

        private HttpStatus httpStatus= HttpStatus.OK;

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }
    }

    public OperationResponse(ResponseStatusEnum status){
        this(status,status.name());
    }
    private ResponseStatusEnum status= ResponseStatusEnum.SUCCESS;
    private String message;

    public ResponseEntity<OperationResponse> makeResponseEntity() {
        return new ResponseEntity<OperationResponse>(this, status.getHttpStatus());
    }

}
