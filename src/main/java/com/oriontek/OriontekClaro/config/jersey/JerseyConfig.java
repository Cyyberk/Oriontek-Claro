package com.oriontek.OriontekClaro.config.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.oriontek.OriontekClaro.exceptionHandlers.GeneralExceptionMapper;
import com.oriontek.OriontekClaro.exceptionHandlers.MyApiExceptionMapper;
import com.oriontek.OriontekClaro.joke.JokeResource;

@Configuration
public class JerseyConfig  extends ResourceConfig{
    
    public JerseyConfig(){
        
        register(JokeResource.class); //Controlador creado para este ejercicio
        register(GeneralExceptionMapper.class); //Manejador de excepciones generales que creé
        register(MyApiExceptionMapper.class); //Manejador de excepciones personalizadas

    }
}
