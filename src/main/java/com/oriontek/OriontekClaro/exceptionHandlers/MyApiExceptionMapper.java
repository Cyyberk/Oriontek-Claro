package com.oriontek.OriontekClaro.exceptionHandlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MyApiExceptionMapper implements ExceptionMapper<MyApiException> {

    @Override
    public Response toResponse(MyApiException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        body.put("status", ex.getStatus());
        body.put("timestamp", LocalDateTime.now());

        return Response.status(ex.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
        
    }
    
}
