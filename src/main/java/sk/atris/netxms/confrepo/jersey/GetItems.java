package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.exceptions.ItemRequestJsonParserException;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;
import sk.atris.netxms.confrepo.service.parser.ItemRequestJsonParser;
import sk.atris.netxms.confrepo.service.supplier.ItemSupplier;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Path("/get-items")
public final class GetItems {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public String getHandler(InputStream incomingData) {
        log.info("POST to '/get-items' received, processing it.");

        List<RequestedConfigItem> requestedConfigItems;
        try {
            requestedConfigItems = ItemRequestJsonParser.getInstance().parse(incomingData);
        } catch (IOException | ItemRequestJsonParserException e) {
            return e.getMessage();
        }

        // TODO: implement this
        // 1 - parse the incoming JSON \//
        // 2 - build a list of RequestedConfigItem objects \//
        // 3 - pass that list to ItemSupplier \//
        // 4 - construct xml
        // 5 - ??
        // 6 - Profit

        try {
            return ItemSupplier.getInstance().getItemsXml(requestedConfigItems);
        } catch (ConfigItemNotFoundException e) {
            return e.getMessage();
        }
    }
}
