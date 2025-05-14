package com.oriontek.OriontekClaro.joke.exceptions;

import com.oriontek.OriontekClaro.exceptionHandlers.MyApiException;

public class JokeMinCountLimitException extends MyApiException {

    public JokeMinCountLimitException(long jokeId) {
        super("The amount of likes/dislike for the joke with ID: " + jokeId + " is already 0. The counter cannot be reduced anymore.", 404, 
        "JOKE_EXCEEDED_MIN_LIMIT");
    }
    
}
