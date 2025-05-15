package com.oriontek.OriontekClaro.joke;

import org.springframework.stereotype.Component;
import com.oriontek.OriontekClaro.joke.dto.ExternalJokeDTO;
import com.oriontek.OriontekClaro.joke.dto.TotalJokeInteractionsDTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Component
@Path("/jokes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JokeResource {
    
    private JokeService jokeService;

    public JokeResource(JokeService jokeService){
        this.jokeService = jokeService;
    }

    @GET
    @Path("/")
    public Response getAllJokes(){
        return Response.ok(jokeService.getAllJokes()).build();
    }

    @GET
    @Path("/{id}")
    public Response getJoke(@PathParam("id") long id){
        Joke joke = jokeService.getJoke(id);
        return Response.ok(joke).build();
    }

    @POST
    @Path("/create")
    public Response createJoke(Joke joke){
        Joke newJoke = jokeService.createJoke(joke);
        return Response.status(201).entity(newJoke).build();      
    } 

    @DELETE
    @Path("/{id}/delete")
    public Response deleteJoke(@PathParam("id") long id){
        jokeService.deleteJoke(id);
        return Response.status(204).build();
    }

    @PATCH
    @Path("/{id}/edit")
    public Response updateJoke(@PathParam("id") long id, Joke updatedJoke){
        Joke joke = jokeService.updateJoke(id, updatedJoke);
        return Response.ok(joke).build();
    }

    @GET
    @Path("/{id}/like")
    public Response likeJoke(@PathParam("id") long id, @QueryParam("action") String action){
        if(action.equals("add")) 
            return Response.ok(jokeService.likeJoke(id)).build();
        else if(action.equals("remove")) 
            return Response.ok(jokeService.removeLikeJoke(id)).build();
        return Response.status(404).build();
    }

    @GET
    @Path("/{id}/dislike")
    public Response dislikeJoke(@PathParam("id") long id, @QueryParam("action") String action){
        if(action.equals("add")) 
            return Response.ok(jokeService.dislikeJoke(id)).build();
        else if(action.equals("remove")) 
            return Response.ok(jokeService.removeDislikeJoke(id)).build();
        return Response.status(404).build();
    }

    @POST
    @Path("/generate")
    public Response generateRandomJoke(){
        // Utilizando la api externa para crear nuevas bromas random para nuestra app.
        ExternalJokeDTO externalAPIJoke = jokeService.generateRandomJoke(); 
        if(externalAPIJoke != null) 
        {
            Joke joke = new Joke();
            joke.setMessage(externalAPIJoke.getValue());
            jokeService.createJoke(joke);
            return Response.status(201).entity(joke).build();
        }
        return Response.status(404).build();
    }

    @GET
    @Path("/{id}/interactions")
    public Response getTotalInteractions(@PathParam("id") long id){
        int total = jokeService.getTotalInteractions(id);
        TotalJokeInteractionsDTO result = new TotalJokeInteractionsDTO((long) id, (long) total);
        return Response.ok().entity(result).build();
    }

}
