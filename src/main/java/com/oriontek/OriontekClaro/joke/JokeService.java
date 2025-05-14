package com.oriontek.OriontekClaro.joke;

import java.util.*;

import org.springframework.stereotype.Service;

import com.oriontek.OriontekClaro.joke.exceptions.JokeMinCountLimitException;
import com.oriontek.OriontekClaro.joke.exceptions.JokeNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class JokeService implements DefaultJokeService {

    private JokeRepository jokeRepository;

    public JokeService(JokeRepository jokeRepository){
        this.jokeRepository = jokeRepository;
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
    
}
