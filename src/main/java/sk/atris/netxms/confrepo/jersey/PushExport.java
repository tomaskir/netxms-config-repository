package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotLoadedException;
import sk.atris.netxms.confrepo.exceptions.NetxmsXmlConfigParserException;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfig;
import sk.atris.netxms.confrepo.service.merger.ConfigMerger;
import sk.atris.netxms.confrepo.service.parser.NetxmsXmlConfigParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Slf4j
@Path("/push-export")
public final class PushExport {
    @POST
    @Consumes(MediaType.TEXT_XML)
    public Response postHandler(@QueryParam("accessToken") String providedAccessToken, InputStream incomingData) {
        NetxmsConfig receivedNetxmsConfig;

        log.info("POST to '/push-export' received, processing it.");

        try {
            WriteAccessValidator.getInstance().check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/push-export' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/push-export' POST.");
            return Response.status(403).entity("Missing or invalid access token.").build();
        } catch (AccessTokenNotLoadedException e) {
            String responseString = "No ReadWrite token configured in '" + ApplicationConfiguration.CONFIG_FILE_NAME.toString() + "', or the file was not found. " +
                    "Please configure a ReadWrite token and restart the application.";

            log.info("Sending HTTP.500 in answer to '/push-export' POST.");
            return Response.status(500).entity(responseString).build();
        }

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
