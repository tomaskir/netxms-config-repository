package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.service.supplier.AvailableItemsSupplier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/get-available-items")
public final class GetAvailableItems {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandler() {
        log.info("GET to '/get-available-items' received, processing it.");

        String responseJson = AvailableItemsSupplier.getInstance().getAllAvailableItemsJson(NetxmsConfigRepository.getInstance());

        log.info("Sending HTTP.200 in answer to '/get-available-items' GET.");
        return Response.ok().entity(responseJson).build();
    }
}
