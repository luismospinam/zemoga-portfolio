package com.zemoga.portfolio.controller;

import com.zemoga.portfolio.Exception.CustomPortfolioException;
import com.zemoga.portfolio.Exception.PortfolioNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    @ExceptionHandler(CustomPortfolioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> invalidPortfolioError(CustomPortfolioException exception) {
        return prepareErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }


    @ExceptionHandler(PortfolioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> PortfolioNotFoundError(PortfolioNotFoundException exception) {
        return prepareErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }


    private Map<String, Object> prepareErrorResponse(HttpStatus status, String message) {
        var errorAttributes = new LinkedHashMap<String, Object>();

        errorAttributes.put("timestamp", LocalDateTime.now());
        errorAttributes.put("Error", status.getReasonPhrase());
        errorAttributes.put("status", status.value());
        errorAttributes.put("message", message);

        return errorAttributes;
    }
}
