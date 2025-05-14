package com.oriontek.OriontekClaro.exceptionHandlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

//Manejador de excepciones generales, que se encargara de las excepciones fuera del control de mis personalizadas.
@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
       
       Map<String, Object> error = new HashMap<>(); //Mapa para estructurar la respuesta del error
       error.put("error", "Internal Server Error");
       error.put("message", ex.getMessage() != null ? ex.getMessage() : "An unknown error has happened.");
       error.put("timestamp", LocalDateTime.now().toString()); //Momento en el que se registro la excepcion 
       error.put("exceptionType", ex.getClass().getSimpleName()); //Obtener el tipo de excepcion lanzada

       return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .type(MediaType.APPLICATION_JSON)
        .entity(error)
        .build();
    }
    
}
