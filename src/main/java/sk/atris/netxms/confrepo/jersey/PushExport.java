package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.NetxmsXmlConfigParserException;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfig;
import sk.atris.netxms.confrepo.service.merger.ConfigMerger;
import sk.atris.netxms.confrepo.service.parser.NetxmsXmlConfigParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Slf4j
@Path("/push-export")
public final class PushExport {
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response postHandler(InputStream incomingData) {
        NetxmsConfig receivedNetxmsConfig;

        log.info("POST to '/push-export' received, processing it.");

        try {
            receivedNetxmsConfig = NetxmsXmlConfigParser.getInstance().parse(incomingData);
        } catch (NetxmsXmlConfigParserException e) {
            log.info("Sending HTTP.400 in answer to '/push-export' POST.");

            if (e.getCause() != null)
                return Response.status(400).entity(e.getCause().getMessage()).build();
            else
                return Response.status(400).entity(e.getMessage()).build();
        }
        ConfigMerger.getInstance().mergeConfiguration(receivedNetxmsConfig);

        log.info("Sending HTTP.200 in answer to '/push-export' POST.");
        return Response.ok().build();
    }
}
