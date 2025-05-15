package com.oriontek.OriontekClaro.joke;

import java.util.*;

import org.springframework.stereotype.Service;

import com.oriontek.OriontekClaro.calculator.CalculatorClient;
import com.oriontek.OriontekClaro.joke.dto.ExternalJokeDTO;
import com.oriontek.OriontekClaro.joke.exceptions.JokeMinCountLimitException;
import com.oriontek.OriontekClaro.joke.exceptions.JokeNotFoundException;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Service
public class JokeService implements DefaultJokeService {

    private final JokeRepository jokeRepository;
    private final Client client; 
    private final CalculatorClient calculator;
    private static final String JOKE_API_URL = "https://api.chucknorris.io/jokes/random";

    public JokeService(JokeRepository jokeRepository, Client client, CalculatorClient calculator){
        this.jokeRepository = jokeRepository;
        this.client = client;
        this.calculator = calculator;
    }

    @Override
    public List<Joke> getAllJokes() {
        return jokeRepository.findAll();
    }

    @Override
    public Joke getJoke(long jokeId) {
        return jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
    }

    @Override
    @Transactional
    public Joke createJoke(Joke joke) {
        return jokeRepository.save(joke);
    }

    // Funciones para trabajar los likes de una broma.

    @Override
    @Transactional
    public Joke likeJoke(long jokeId) {
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
        joke.setLikes(joke.getLikes()+1);   
        return jokeRepository.save(joke);
    }

    @Override
    @Transactional
    public Joke removeLikeJoke(long jokeId) {  
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
        if(joke.getLikes() == 0) throw new JokeMinCountLimitException(jokeId);
        joke.setLikes(joke.getLikes()-1);
        
        return jokeRepository.save(joke);
    }
    
    // Funciones para trabajar los dislikes de una broma.

    @Override
    @Transactional
    public Joke dislikeJoke(long jokeId) {
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
        joke.setDislikes(joke.getDislikes() + 1);
        return jokeRepository.save(joke);
    }

    @Override
    @Transactional
    public Joke removeDislikeJoke(long jokeId) {
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
        if(joke.getDislikes() == 0) throw new JokeMinCountLimitException(jokeId);
        joke.setDislikes(joke.getDislikes()-1);
    
        return jokeRepository.save(joke);
    }
    
    @Override
    @Transactional
    public Joke deleteJoke(long jokeId) {
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
        jokeRepository.delete(joke);
        return joke;
    }

    @Override
    @Transactional
    public Joke updateJoke(long jokeId, Joke updatedJoke) {
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
        joke.setMessage(updatedJoke.getMessage());
        return jokeRepository.save(joke);
    }
    

    // Aqui estaremos usando el API externa de bromas sobre chuck norris para traernos una broma random para nuestra app.
    public ExternalJokeDTO generateRandomJoke(){  
        WebTarget target = client.target(JOKE_API_URL);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        ExternalJokeDTO randomJoke = null;
        
        randomJoke = response.readEntity(ExternalJokeDTO.class);
        response.close();
       
        return randomJoke;
    }

    // Aqui usamos la calculadora SOAP para calcular el total de likes y dislikes en una broma
    public int getTotalInteractions(long jokeId){
        Joke joke = jokeRepository.findById(jokeId).orElseThrow(()-> new JokeNotFoundException(jokeId));
        return calculator.add((int) joke.getLikes(), (int) joke.getDislikes());
     }


}
