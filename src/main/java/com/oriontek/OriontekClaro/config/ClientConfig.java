package com.oriontek.OriontekClaro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

/* Creé esta configuracion para permitir injeccion de dependencia para 
la clase Client que llama a una API externa, y con esto lograr hacer tests unitarios
*/

@Configuration
public class ClientConfig {

    @Bean
    Client jaxRClient(){
        return ClientBuilder.newBuilder().build();
    }

}
