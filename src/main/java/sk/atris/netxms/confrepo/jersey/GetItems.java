package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;
import sk.atris.netxms.confrepo.exceptions.*;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;
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
    @Produces(MediaType.APPLICATION_XML)
    public Response getHandler(@QueryParam("accessToken") String providedAccessToken, InputStream incomingData) {
        String xmlString;
        List<RequestedConfigItem> requestedConfigItems;

        log.info("POST to '/get-items' received, processing it.");

        try {
            CheckAccessToken.getInstance().check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/get-items' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/get-items' POST.");
            return Response.status(403).entity("Missing or invalid access token.").build();
        }

        try {
            requestedConfigItems = ItemRequestJsonParser.getInstance().parse(incomingData);
        } catch (IOException | ItemRequestJsonParserException e) {
            log.info("Sending HTTP.400 in answer to '/get-items' POST.");

            if (e.getCause() != null)
                return Response.status(400).entity(e.getCause().getMessage()).build();
            else
                return Response.status(400).entity(e.getMessage()).build();
        }

        try {
            xmlString = ItemSupplier.getInstance().getItemsXml(requestedConfigItems);
        } catch (ConfigItemNotFoundException | JDOMException | IOException | NoConfigItemsRequestedException | RevisionNotFoundException e) {
            log.info("Sending HTTP.400 in answer to '/get-items' POST.");

            if (e.getCause() != null)
                return Response.status(400).entity(e.getCause().getMessage()).build();
            else
                return Response.status(400).entity(e.getMessage()).build();
        }

        log.info("Sending HTTP.200 in answer to '/get-items' POST.");
        return Response.ok().entity(xmlString).build();
    }
}
