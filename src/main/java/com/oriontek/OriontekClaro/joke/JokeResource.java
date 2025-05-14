package com.oriontek.OriontekClaro.joke;

import org.springframework.stereotype.Component;


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
        return Response.ok(newJoke).build();      
    } 

    @DELETE
    @Path("/{id}/delete")
    public Response deleteJoke(@PathParam("id") long id){
        Joke joke = jokeService.deleteJoke(id);
        return Response.ok(joke).build();
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
        return null;
    }

    @GET
    @Path("/{id}/dislike")
    public Response dislikeJoke(@PathParam("id") long id, @QueryParam("action") String action){
        if(action.equals("add")) 
            return Response.ok(jokeService.dislikeJoke(id)).build();
        else if(action.equals("remove")) 
            return Response.ok(jokeService.removeDislikeJoke(id)).build();
        return null;
    }

    

}
