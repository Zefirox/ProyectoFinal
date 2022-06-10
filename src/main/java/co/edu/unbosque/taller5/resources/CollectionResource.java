package co.edu.unbosque.taller5.resources;

import co.edu.unbosque.taller5.dtos.Collection;
import co.edu.unbosque.taller5.services.CollectionService;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Path("/collections")
public class CollectionResource {

    @Context
    ServletContext context;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost/proyectofinal";

    // Database credentials
    static final String USER = "postgres";
    static final String PASS = "Zeref29714526?";

    @POST
    @Path("/{userapp}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCollection(@FormParam("name") String name,
                                     @FormParam("description") String description,
                                     @FormParam("category") String category,
                                     @PathParam("userapp") String userapp) {
        Connection conn = null;

        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Collection newcollection = null;
        try {
            newcollection = new CollectionService().createCollection(name,description,category,userapp,conn).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (newcollection!=null){
            return Response.ok().entity(newcollection).build();
        }

        return Response.serverError().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCollection(@PathParam("id") int colletionId){

        Connection conn = null;
        String collectionName = "";

        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Collection collection = new CollectionService().getCollection(conn,colletionId).get();

            collectionName = collection.getName();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok().entity(collectionName).build();
    }
}