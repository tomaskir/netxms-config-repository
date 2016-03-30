package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.service.supplier.AvailableItemsSupplier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/get-available-items")
public final class GetAvailableItems {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandler(@QueryParam("accessToken") String providedAccessToken) {
        String responseJson;

        log.info("GET to '/get-available-items' received, processing it.");

        try {
            ReadAccessValidator.getInstance().check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/get-available-items' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/get-available-items' GET.");
            return Response.status(403).entity("Missing or invalid access token.").build();
        }

        responseJson = AvailableItemsSupplier.getInstance().getAllAvailableItemsJson(NetxmsConfigRepository.getInstance());

        log.info("Sending HTTP.200 in answer to '/get-available-items' GET.");
        return Response.ok().entity(responseJson).build();
    }
}
