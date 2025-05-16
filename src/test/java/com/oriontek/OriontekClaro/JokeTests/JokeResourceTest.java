package com.oriontek.OriontekClaro.JokeTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.oriontek.OriontekClaro.joke.Joke;
import com.oriontek.OriontekClaro.joke.JokeResource;
import com.oriontek.OriontekClaro.joke.JokeService;
import com.oriontek.OriontekClaro.joke.dto.ExternalJokeDTO;
import com.oriontek.OriontekClaro.joke.dto.TotalJokeInteractionsDTO;

import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
public class JokeResourceTest{
    

   @Mock
   private JokeService jokeService;

    @InjectMocks
    private JokeResource jokeResource;



    Instant testCreatedAt = Instant.now();
    Joke mockJoke = new Joke(null, "new joke", 0L,0L, null, null);
   
   
    @Test
    void mustGetAllJokes(){

        List<Joke> expectedJokeListResponse = List.of(mockJoke, mockJoke);
        when(jokeService.getAllJokes()).thenReturn(expectedJokeListResponse);

        // Test
        Response response = jokeResource.getAllJokes();
        List<Joke> result = (List<Joke>) response.getEntity();

        // Verificando
        assertEquals(2, result.size());
        verify(jokeService, times(1)).getAllJokes(); // revisando si el metodo fue llamado 1 vez

    }

    @Test
    void mustGetRequestedJoke(){
   
        Joke foundJoke = new Joke(1L, "new joke", 0L,0L, testCreatedAt, testCreatedAt);
        when(jokeService.getJoke(1L)).thenReturn(foundJoke);

        Response response = jokeResource.getJoke(1L);

        assertEquals(200, response.getStatus());
        assertEquals(foundJoke, response.getEntity()); // comparando la respuesta que espero con la que me fue dada por el endpoint
    }


    @Test
    void mustCreateJoke(){

        Joke newJoke = new Joke(1L, "new joke", 0L,0L, testCreatedAt, testCreatedAt);
        when(jokeService.createJoke(any())).thenReturn(newJoke);

        Response response = jokeResource.createJoke(mockJoke);

        // Verificando que de codigo de que se creó
        assertEquals(201, response.getStatus());
        assertEquals(newJoke, response.getEntity());

    }
    
    @Test
    void mustDeleteJoke(){

        Joke deletedJoke = new Joke(1L, "deleted joke", 0L,0L, testCreatedAt, testCreatedAt);
        when(jokeService.deleteJoke(1L)).thenReturn(deletedJoke);

        Response response = jokeResource.deleteJoke(1L);

        assertEquals(200, response.getStatus());
    }

    @Test
    void mustUpdateJoke(){

        Joke updatedJoke = new Joke(1L, "updated joke", 0L,0L, testCreatedAt, Instant.now());
        when(jokeService.updateJoke(1L, mockJoke)).thenReturn(updatedJoke);

        Response response = jokeResource.updateJoke(1L, mockJoke);

        assertEquals(200, response.getStatus());
        assertEquals(updatedJoke, response.getEntity());

    }

    @Test
    void mustLikeJoke(){
        
        Joke expectedLikedJoke= new Joke(1L, "liked joke", 1L,0L, testCreatedAt, Instant.now());
        Joke expectedRemovedLikedJoke= new Joke(1L, "removed liked joke", 0L,0L, testCreatedAt, Instant.now());
        
        when(jokeService.likeJoke(1L)).thenReturn(expectedLikedJoke);
        when(jokeService.removeLikeJoke(1L)).thenReturn(expectedRemovedLikedJoke);

        Response responseLike = jokeResource.likeJoke(1L, "add");
        Response responseRemoveLike = jokeResource.likeJoke(1L, "remove");

        // Verificaciones para cuando se le dio like
        assertEquals(200, responseLike.getStatus());
        assertEquals(expectedLikedJoke, responseLike.getEntity());
        assertNotEquals(mockJoke.getUpdatedAt(), ((Joke) responseLike.getEntity()).getUpdatedAt()); // Verificando si cambio la fecha de modificacion

        // Verificaciones para cuando se removio el like
        assertEquals(200, responseRemoveLike.getStatus());
        assertEquals(expectedRemovedLikedJoke, responseRemoveLike.getEntity());
        assertNotEquals(mockJoke.getUpdatedAt(), ((Joke) responseRemoveLike.getEntity()).getUpdatedAt());
        
    }

  
    @Test
    void mustDislikeJoke(){
        
        Joke expectedDislikedJoke= new Joke(1L, "disliked joke", 1L,0L, testCreatedAt, Instant.now());
        Joke expectedRemovedDislikedJoke= new Joke(1L, "removed disliked joke", 0L,0L, testCreatedAt, Instant.now());
        
        when(jokeService.dislikeJoke(1L)).thenReturn(expectedDislikedJoke);
        when(jokeService.removeDislikeJoke(1L)).thenReturn(expectedRemovedDislikedJoke);

        Response responseDislike = jokeResource.dislikeJoke(1L, "add");
        Response responseRemoveDislike = jokeResource.dislikeJoke(1L, "remove");

        // Verificaciones para cuando se le dio like
        assertEquals(200, responseDislike.getStatus());
        assertEquals(expectedDislikedJoke, responseDislike.getEntity());
        assertNotEquals(mockJoke.getUpdatedAt(), ((Joke) responseDislike.getEntity()).getUpdatedAt()); // Verificando si cambio la fecha de modificacion

        // Verificaciones para cuando se removio el like
        assertEquals(200, responseRemoveDislike.getStatus());
        assertEquals(expectedRemovedDislikedJoke, responseRemoveDislike.getEntity());
        assertNotEquals(mockJoke.getUpdatedAt(), ((Joke) responseRemoveDislike.getEntity()).getUpdatedAt());
        
    }

    @Test
    void mustErrorLikingAndDislikingJoke(){

        Response responseLike = jokeResource.likeJoke(1L, "invalid action"); //fail if no valid action were gived;
        Response responseDislike = jokeResource.dislikeJoke(1L, "invalid action"); //fail if no valid action were gived;
       
        assertEquals(404, responseLike.getStatus());
        assertEquals(404, responseDislike.getStatus());
    }
  
   
    @Test
    void mustGenerateRandomJoke(){

        ExternalJokeDTO expectedRandomJoke = new ExternalJokeDTO("random joke");
        mockJoke.setMessage(expectedRandomJoke.getValue());

        Joke createdJoke = new Joke(1L, "random joke", 0L,0L, testCreatedAt, testCreatedAt);

        when(jokeService.generateRandomJoke()).thenReturn(expectedRandomJoke);
        when(jokeService.createJoke(mockJoke)).thenReturn(createdJoke);
        
        Response response = jokeResource.generateRandomJoke();

        assertEquals(201, response.getStatus());
        assertEquals(createdJoke.getMessage(), expectedRandomJoke.getValue());
        
    }

     @Test
    void mustErrorGeneratingRandomJoke(){

        when(jokeService.generateRandomJoke()).thenReturn(null);
        Response response = jokeResource.generateRandomJoke();
        assertEquals(404, response.getStatus());
    }

    @Test
    void mustCountTotalInteractions(){
        
        TotalJokeInteractionsDTO total = new TotalJokeInteractionsDTO(1L, 10);

        when(jokeService.getTotalInteractions(1L)).thenReturn(10); // Total de likes y dislikes

        Response response = jokeResource.getTotalInteractions(1L);
        assertEquals(200, response.getStatus());
        assertEquals(total, response.getEntity());
    }


}
