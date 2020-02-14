package com.github.recruitment;

import com.github.recruitment.model.ErrorDetailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class ErrorHandlerControllerAdvice {

    private static final String SERVICE_NOT_AVAILABLE = "Service is currently unavailable, please try again later.";

    @ExceptionHandler ( Exception.class )
    public ResponseEntity handleGenericException( Exception exception ) {
        log.warn( exception.getMessage() );
        return createOutputInternalServerError();
    }

    @ExceptionHandler ( HttpServerErrorException.class )
    public ResponseEntity handleHttpServerErrorException( HttpServerErrorException exception ) {
        log.warn( exception.getMessage() );
        return createOutputInternalServerError();
    }

    @ExceptionHandler ( UnknownHttpStatusCodeException.class )
    public ResponseEntity handleUnknownHttpStatusCodeException( UnknownHttpStatusCodeException exception ) {
        log.warn( exception.getMessage() );
        return createOutputInternalServerError();
    }

    @ExceptionHandler ( HttpClientErrorException.class )
    public ResponseEntity handleHttpClientErrorException( HttpClientErrorException httpClientException ) {
        log.warn( httpClientException.getMessage() );
        switch ( httpClientException.getStatusCode() ) {
            case NOT_FOUND:
                return ResponseEntity
                        .status( httpClientException.getStatusCode() )
                        .body( ErrorDetailMessage.builder()
                                .description( httpClientException.getStatusText() )
                                .build() );
            default:
                return createOutputInternalServerError();
        }
    }

    private ResponseEntity createOutputInternalServerError() {
        return ResponseEntity
                .status( INTERNAL_SERVER_ERROR )
                .body( ErrorDetailMessage.builder()
                        .code( "500" )
                        .description( SERVICE_NOT_AVAILABLE )
                        .build() );
    }
}
