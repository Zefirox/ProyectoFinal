package co.edu.unbosque.taller5.resources;

import co.edu.unbosque.taller5.services.WalletService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/wallet")
public class WalletHistoryResource {
    @GET
    @Path("/{email}")
    @Produces("text/plain")
    public Response getWallet(@PathParam("email") String username) {
        float fcoins = new WalletService().getFcoins(username);

        return Response.ok().entity(fcoins).build();
    }
}