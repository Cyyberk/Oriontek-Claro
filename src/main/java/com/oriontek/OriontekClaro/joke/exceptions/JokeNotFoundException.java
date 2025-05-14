package com.oriontek.OriontekClaro.joke.exceptions;

import com.oriontek.OriontekClaro.exceptionHandlers.MyApiException;

public class JokeNotFoundException extends MyApiException{
    public JokeNotFoundException(long jokeId){
        super("Joke with the ID " + jokeId + " not found.", 404, "JOKE_NOT_FOUND");
    }
}
