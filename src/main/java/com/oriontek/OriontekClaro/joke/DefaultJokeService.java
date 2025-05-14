package com.oriontek.OriontekClaro.joke;

import java.util.*;

public interface DefaultJokeService {
    
    public List<Joke> getAllJokes();
    public Joke getJoke(long jokeId);
    public Joke createJoke(Joke newJoke);
    public Joke likeJoke(long jokeId);
    public Joke dislikeJoke(long jokeId);
    public Joke removeLikeJoke(long jokeId);
    public Joke removeDislikeJoke(long jokeId);    
    public Joke deleteJoke(long jokeId);
    public Joke updateJoke(long jokeId, Joke updatedJoke);

}
