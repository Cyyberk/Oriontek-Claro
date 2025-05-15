package com.oriontek.OriontekClaro.joke.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // ignoramos los campos que traera la api que no vayamos a necesitar
@NoArgsConstructor
public class ExternalJokeDTO {

    private String value;
    
}
