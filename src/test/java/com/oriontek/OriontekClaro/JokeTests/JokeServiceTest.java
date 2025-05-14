package com.oriontek.OriontekClaro.JokeTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oriontek.OriontekClaro.joke.Joke;
import com.oriontek.OriontekClaro.joke.JokeRepository;
import com.oriontek.OriontekClaro.joke.JokeService;
import com.oriontek.OriontekClaro.joke.exceptions.JokeMinCountLimitException;
import com.oriontek.OriontekClaro.joke.exceptions.JokeNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JokeServiceTest {
    
    @Mock
    private JokeRepository jokeRepository;

    @InjectMocks
    private JokeService jokeService;

    Joke mockJoke = new Joke(null, "test message", 0L,0L, null, null);
    Instant testCreatedAt = Instant.now();

    @Test
    void mustCreateJoke(){

        // Lo que nos deberia retornar el repositorio al crear la broma
        Joke expectedReturnNewJoke = new Joke(1L, "test message", 0L, 0L, testCreatedAt, testCreatedAt);

        // Mock de la dependencia del repositorio
        when(jokeRepository.save(mockJoke)).thenReturn(expectedReturnNewJoke);

        // Testing
        Joke createdJoke = jokeService.createJoke(mockJoke); //createJoke() debe devolver la broma una vez creada
        assertEquals(1L, createdJoke.getId()); // Probando si la broma creada tiene un id generado
        assertEquals(testCreatedAt, createdJoke.getCreatedAt()); // Probando si la broma creada tiene el tiempo de creacion generado
        assertEquals(createdJoke, expectedReturnNewJoke);

    }

    
    @Test
    void mustGetRequestedJoke(){

        Joke expectedReturnExistingJoke1 = new Joke(1L, "test message", 1L, 0L, testCreatedAt, testCreatedAt);
        Joke expectedReturnExistingJoke2 = new Joke(2L, "test message 2", 2L, 5L, testCreatedAt, testCreatedAt);
        
        when(jokeRepository.findById(1L)).thenReturn(Optional.of(expectedReturnExistingJoke1));
        when(jokeRepository.findById(2L)).thenReturn(Optional.of(expectedReturnExistingJoke2));

        Joke joke1 = jokeService.getJoke(1L);
        Joke joke2 = jokeService.getJoke(2L);

        assertEquals(1L, joke1.getId());
        assertEquals(2L, joke2.getId());

        // testeando de que los likes y dislikes se esten leyendo bien
        assertNotEquals(0L, joke1.getLikes()); 
        assertEquals(5L, joke2.getDislikes());
    }

    
    @Test
    void mustGetAllJokes(){

        List<Joke> expectedReturnJokeList = List.of(mockJoke, mockJoke);
        when(jokeRepository.findAll()).thenReturn(expectedReturnJokeList);

        List<Joke> jokeList = jokeService.getAllJokes();
        assertEquals(2, jokeList.size()); // Testeando que la lista obtenida tenga la cantidad correcta de elementos (2).
    }

    
    @Test
    void mustLikeAndDislikeJokeAndRemoveLikeAndDislike(){
        
        Joke expectedReturnExistingJoke = new Joke(1L, "test message", 0L, 0L, testCreatedAt, testCreatedAt);
        
        // Darle like a la broma debe incrementar el contador de likes en 1
        when(jokeRepository.findById(1L)).thenReturn(Optional.of(expectedReturnExistingJoke));
        when(jokeRepository.save(expectedReturnExistingJoke)).thenReturn(expectedReturnExistingJoke);

       
        // Añadir like 2 veces
        Joke mockJoke = jokeService.likeJoke(1L);
        jokeService.likeJoke(1L);
        assertEquals(2L, mockJoke.getLikes()); //el contador de likes debio incrementar en 2 dentro del servicio

        // Remover like 1 vez
        mockJoke = jokeService.removeLikeJoke(1L); //el contador de likes debio decrementar en 1 dentro del servicio
        assertEquals(1L, mockJoke.getLikes());

        // Añadir dislike 2 veces
        mockJoke = jokeService.dislikeJoke(1L);
        jokeService.dislikeJoke(1L);
        assertEquals(2L, mockJoke.getDislikes());

        // Remover dislike 1 vez
        mockJoke = jokeService.removeDislikeJoke(1L);
        assertEquals(1L, mockJoke.getDislikes());

    }


    @Test
    void mustErrorIfRemovingLikesAndDislikesFromZero(){

        Joke expectedReturnExistingJoke = new Joke(1L, "test message", 0L, 0L, testCreatedAt, testCreatedAt);
        
        when(jokeRepository.findById(1L)).thenReturn(Optional.of(expectedReturnExistingJoke));
        
        assertThrows(JokeMinCountLimitException.class, ()-> jokeService.removeLikeJoke(1L)); // Ya tiene 0 likes, deberia lanzar error
        assertThrows(JokeMinCountLimitException.class, ()-> jokeService.removeDislikeJoke(1L)); // Lo mismo con los dislikes
    }

    @Test
    void mustErrorIfJokeNotExists(){
        when(jokeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(JokeNotFoundException.class, ()-> jokeService.getJoke(1L));
    }


    @Test
    void mustDeleteJoke(){

        Joke expectedReturnDeletedJoke = new Joke(1L, "test message", 0L, 0L, testCreatedAt, testCreatedAt);
        
        when(jokeRepository.findById(1L)).thenReturn(Optional.of(expectedReturnDeletedJoke));
        
        Joke deletedJoke = jokeService.deleteJoke(1L);

        // Ya que delete retorna un void, solo verificaremos si el delete fue llamado en el repositorio dentro del servicio exactamente 1 vez
        verify(jokeRepository, times(1)).delete(deletedJoke); 

        assertEquals(expectedReturnDeletedJoke, deletedJoke); 
    }

    @Test
    void mustUpdateJoke(){

        Joke originalJoke = new Joke(1L, "original", 0L, 0L, testCreatedAt, testCreatedAt);
        Joke expectedReturnUpdatedJoke = new Joke(1L, "updated", 0L, 0L, testCreatedAt, Instant.now());

        when(jokeRepository.findById(1L)).thenReturn(Optional.of(originalJoke));

        jokeService.updateJoke(originalJoke.getId(), expectedReturnUpdatedJoke);

        assertEquals(expectedReturnUpdatedJoke.getMessage(), originalJoke.getMessage()); // Verificando si la broma original cambio el mensaje
        assertEquals(expectedReturnUpdatedJoke.getCreatedAt(), testCreatedAt); // Verificando si no fue modificada la fecha de creacion de la broma original
        assertNotEquals(expectedReturnUpdatedJoke.getUpdatedAt(), originalJoke.getUpdatedAt()); // Verificando si se cambio la ultima fecha de modificacion
    }

}
