package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.exceptions.ItemRequestJsonParserException;
import sk.atris.netxms.confrepo.exceptions.NoConfigItemsRequestedException;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;
import sk.atris.netxms.confrepo.service.parser.ItemRequestJsonParser;
import sk.atris.netxms.confrepo.service.supplier.ItemSupplier;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Path("/get-items")
public final class GetItems {
    private final ItemRequestJsonParser itemRequestJsonParser = ItemRequestJsonParser.getInstance();
    private final ItemSupplier itemSupplier = ItemSupplier.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response getHandler(InputStream incomingData) {
        String xmlString;
        List<RequestedConfigItem> requestedConfigItems;

        log.info("POST to '/get-items' received, processing it.");

        try {
            requestedConfigItems = itemRequestJsonParser.parse(incomingData);
        } catch (IOException | ItemRequestJsonParserException e) {
            log.info("Sending HTTP.400 in answer to '/get-items' POST.");

            if (e.getCause() != null)
                return Response.status(400).entity(e.getCause().getMessage()).build();
            else
                return Response.status(400).entity(e.getMessage()).build();
        }

        try {
            xmlString = itemSupplier.getItemsXml(requestedConfigItems);
        } catch (ConfigItemNotFoundException | JDOMException | IOException | NoConfigItemsRequestedException e) {
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
