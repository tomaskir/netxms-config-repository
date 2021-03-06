package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;
import sk.atris.netxms.confrepo.exceptions.*;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;
import sk.atris.netxms.confrepo.service.accessValidator.ReadAccessValidator;
import sk.atris.netxms.confrepo.service.parser.ItemRequestJsonParser;
import sk.atris.netxms.confrepo.service.supplier.ItemSupplier;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Path("/get-items")
public final class GetItems {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_XML)
    public Response getHandler(@QueryParam("accessToken") String providedAccessToken, InputStream incomingData) {
        String xmlString;
        List<RequestedConfigItem> requestedConfigItems;

        log.info("POST to '/get-items' received, processing it.");

        // validate access to the application
        try {
            ReadAccessValidator.getInstance().check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/get-items' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/get-items' POST.");
            return Response.status(403).entity("Missing or invalid access token.").build();
        } catch (AccessTokenNotLoadedException e) {
            String responseString = "No access tokens configured in '" + ApplicationConfiguration.CONFIG_FILE_NAME.toString() + "', or the file was not found. " +
                    "Please configure a ReadOnly or ReadWrite token and restart the application.";

            log.info("Sending HTTP.500 in answer to '/get-items' POST.");
            return Response.status(500).entity(responseString).build();
        }

        // parse the item request JSON
        try {
            requestedConfigItems = ItemRequestJsonParser.getInstance().parse(incomingData);
        } catch (IOException | ItemRequestJsonParserException e) {
            Throwable ex = e;
            while (ex.getCause() != null)
                ex = ex.getCause();

            log.info("Sending HTTP.400 in answer to '/get-items' POST.");
            return Response.status(400).entity(ex.getMessage()).build();
        }

        // build the response XML document
        try {
            xmlString = ItemSupplier.getInstance().getItemsXml(requestedConfigItems);
        } catch (ConfigItemNotFoundException | NoConfigItemsRequestedException | RevisionNotFoundException e) {
            Throwable ex = e;
            while (ex.getCause() != null)
                ex = ex.getCause();

            log.info("Sending HTTP.400 in answer to '/get-items' POST.");
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (RepositoryInitializationException | JDOMException | IOException e) {
            Throwable ex = e;
            while (ex.getCause() != null)
                ex = ex.getCause();

            log.info("Sending HTTP.500 in answer to '/push-export' POST.");
            return Response.status(500).entity(ex.getMessage()).build();
        }

        log.info("Sending HTTP.200 in answer to '/get-items' POST.");
        return Response.ok().entity(xmlString).build();
    }
}
