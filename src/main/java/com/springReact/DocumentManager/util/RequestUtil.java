package com.springReact.DocumentManager.util;

import com.springReact.DocumentManager.constants.Constants;
import com.springReact.DocumentManager.domain.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class RequestUtil {

    public static Response getResponse(HttpServletRequest httpServletRequest, Map<?,?> data, String message, HttpStatus status)
    {
        return new Response(LocalDateTime.now().toString(),status.value(),httpServletRequest.getRequestURI(),
                HttpStatus.valueOf(status.value()),message, Constants.EMPTY_STRING,data);
    }
}
