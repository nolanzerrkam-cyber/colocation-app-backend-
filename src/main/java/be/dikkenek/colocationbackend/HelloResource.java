package com.istasse.hello_world;

import com.istasse.hello_world.dao.TextDAO;
import com.istasse.hello_world.entity.Text;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.awt.*;

@Path("/hello-world")
public class HelloResource {
    private static final EntityManagerFactory emf =Persistence.createEntityManagerFactory("unit");

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello(@PathParam("id") Long id)
    {
        TextDAO dao = new TextDAO(emf.createEntityManager());
        Text txt = Text.findById(dao,id);

        if (txt == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(txt.getMsg()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response Create(Text txt)
    {
        TextDAO dao = new TextDAO(emf.createEntityManager());
        if(txt.Create(dao))
        {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}