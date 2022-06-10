package co.edu.unbosque.taller5.resources;

import co.edu.unbosque.taller5.dtos.Art;
import co.edu.unbosque.taller5.services.ArtService;
import co.edu.unbosque.taller5.services.OwnershipService;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("/arts")
public class ArtResource {

    @Context
    ServletContext context;

    private final String UPLOAD_DIRECTORY = File.separator;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost/proyectofinal";

    // Database credentials
    static final String USER = "postgres";
    static final String PASS = "Zeref29714526?";

    @POST
    @Path("/{email}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(MultipartFormDataInput input,
                               @PathParam("email") String username) {
        String fileName = "";

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

        try {
            // Getting the file from form input
            Map<String, List<InputPart>> formParts = input.getFormDataMap();
            String title = formParts.get("title").get(0).getBodyAsString();
            String price = formParts.get("price").get(0).getBodyAsString();
            String currentCollection = formParts.get("coleccion").get(0).getBodyAsString();
            List<InputPart> inputParts = formParts.get("image");

            String theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvwxyz";
            StringBuilder builder;

            builder = new StringBuilder(16);

            for (int m = 0; m < 16; m++) {
                int myindex = (int)(theAlphaNumericS.length() * Math.random());

                builder.append(theAlphaNumericS.charAt(myindex));
            }
            String newFileName = builder.toString();

            for (InputPart inputPart : inputParts) {
                if (fileName.equals("") || fileName == null) {
                    // Retrieving headers and reading the Content-Disposition header to file name
                    MultivaluedMap<String, String> headers = inputPart.getHeaders();
                    fileName = parseFileName(headers);
                }

                String format = fileName.split("\\.")[1];

                newFileName += "." + format;
            }

            boolean createArt = new ArtService().createArt(username,title,price,newFileName,currentCollection,conn).get();

            if (createArt){
                for (InputPart inputPart : inputParts){
                    InputStream inputStream = inputPart.getBody(InputStream.class,null);

                    saveFile(inputStream,newFileName,currentCollection,context);
                }
            }
            else{
                System.out.println("Error");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok()
                .entity("Image successfully uploaded")
                .build();
    }

    private String parseFileName(MultivaluedMap<String, String> headers) {
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");

        for (String name : contentDispositionHeader) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                String fileName = tmp[1].trim().replaceAll("\"","");
                return fileName;
            }
        }

        return "unknown";
    }

    // Save uploaded file to a defined location on the server
    private void saveFile(InputStream uploadedInputStream, String fileName, String currentCollection, ServletContext context) {
        int read = 0;
        byte[] bytes = new byte[1024];

        try {
            // Complementing servlet path with the relative path on the server
            String uploadPath = context.getRealPath("") + UPLOAD_DIRECTORY+currentCollection;

            // Creating the upload folder, if not exist
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) uploadDir.mkdir();

            // Persisting the file by output stream
            OutputStream outpuStream = new FileOutputStream(uploadPath + File.separator+fileName);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }

            outpuStream.flush();
            outpuStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArtsImage(){

        try {
            List<Art> artPieces = new ArtService().getArtPiece().get();

            return Response.status(200).entity(artPieces).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.serverError().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getOwner(@PathParam("id") int artId){

        String owner = new OwnershipService().getOwner(artId).get();

        if (owner != null){
            return Response.ok().entity(owner).build();
        }

        return Response.serverError().build();

    }

    @PUT
    @Path("/sale")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeSale(@FormParam("title") String name,
                               @FormParam("forsale") boolean forsale){

        boolean change = new ArtService().forSale(name,forsale).get();

        if(change){
            return Response.ok().build();
        }

        return Response.serverError().build();
    }

    @PUT
    @Path("/price")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changePrice(@FormParam("title") String name,
                               @FormParam("newprice") float newPrice){

        boolean change = new ArtService().price(name,newPrice).get();

        if(change){
            return Response.ok().build();
        }

        return Response.serverError().build();
    }

}