package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;
import sk.atris.netxms.confrepo.exceptions.RepositoryInitializationException;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.service.accessValidator.ReadAccessValidator;
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
        log.info("GET to '/get-available-items' received, processing it.");

        // validate access to the application
        try {
            ReadAccessValidator.getInstance().check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/get-available-items' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/get-available-items' GET.");
            return Response.status(403).entity("Missing or invalid access token.").build();
        } catch (AccessTokenNotLoadedException e) {
            String responseString = "No access tokens configured in '" + ApplicationConfiguration.CONFIG_FILE_NAME.toString() + "', or the file was not found. " +
                    "Please configure a ReadOnly or ReadWrite token and restart the application.";

            log.info("Sending HTTP.500 in answer to '/get-available-items' GET.");
            return Response.status(500).entity(responseString).build();
        }

        // build response JSON
        String responseJson;
        try {
            responseJson = AvailableItemsSupplier.getInstance().getAllAvailableItemsJson(NetxmsConfigRepository.getInstance());
        } catch (RepositoryInitializationException e) {
            Throwable ex = e;
            while (ex.getCause() != null)
                ex = ex.getCause();

            log.info("Sending HTTP.500 in answer to '/push-export' POST.");
            return Response.status(500).entity(ex.getMessage()).build();
        }

        log.info("Sending HTTP.200 in answer to '/get-available-items' GET.");
        return Response.ok().entity(responseJson).build();
    }
}
