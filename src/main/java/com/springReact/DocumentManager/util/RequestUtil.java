package com.springReact.DocumentManager.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springReact.DocumentManager.constants.Constants;
import com.springReact.DocumentManager.domain.Response;
import com.springReact.DocumentManager.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.apache.commons.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

public class RequestUtil {

    private static final BiConsumer<HttpServletResponse,Response > writeResponse = (httpServletResponse, response) -> {
        try {
            var outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream,response);
            outputStream.flush();
        }
        catch (Exception exception){
            throw new ApiException(exception.getMessage());
        }
    };

    private static final BiFunction<Exception,HttpStatus, String> errorReason = (exception, httpStatus) -> {
        if (httpStatus.isSameCodeAs(HttpStatus.FORBIDDEN)){
            return "You don't have enough permission";
        }
        if (httpStatus.isSameCodeAs(HttpStatus.UNAUTHORIZED)){
            return "You are not logged in";
        }
        if (exception instanceof DisabledException ||exception instanceof LockedException
                ||exception instanceof BadCredentialsException || exception instanceof CredentialsExpiredException
                ||exception instanceof ApiException){

            return exception.getMessage();
        }
        if(httpStatus.is5xxServerError()){
            return "Internal Serve error occurred";
        }
        else {
            return "An error occurred. Please try again";
        }
    };

    public static Response getResponse(HttpServletRequest httpServletRequest, Map<?,?> data, String message, HttpStatus status)
    {
        return new Response(LocalDateTime.now().toString(),status.value(),httpServletRequest.getRequestURI(),
                HttpStatus.valueOf(status.value()),message, Constants.EMPTY_STRING,data);
    }

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response,Exception exception)
    {
        if(exception instanceof AccessDeniedException)
        {
            var apiResponse = getErrorResponse(request,response,exception,HttpStatus.FORBIDDEN);
            writeResponse.accept(response,apiResponse);
        }
    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus httpStatus) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return new Response(LocalDateTime.now().toString(),httpStatus.value(),request.getRequestURI(),
                HttpStatus.valueOf(httpStatus.value()), errorReason.apply(exception,httpStatus),
//                ExceptionUtil.getRootCauseMessage(exception),//didn't work 7:43:23
                exception.getMessage(),
                Collections.emptyMap());
    }
}
