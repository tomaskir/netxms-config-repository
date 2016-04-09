package sk.atris.netxms.confrepo.jersey;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;
import sk.atris.netxms.confrepo.exceptions.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.ReceivedNetxmsConfig;
import sk.atris.netxms.confrepo.service.accessValidator.WriteAccessValidator;
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
    @Consumes(MediaType.APPLICATION_XML)
    public Response postHandler(@QueryParam("accessToken") String providedAccessToken, InputStream incomingData) {
        ReceivedNetxmsConfig receivedNetxmsConfig;

        log.info("POST to '/push-export' received, processing it.");

        // validate access to the application
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

        // parse the incoming XML document
        try {
            receivedNetxmsConfig = NetxmsXmlConfigParser.getInstance().parse(incomingData);
        } catch (NetxmsXmlConfigParserException e) {
            Throwable ex = e;
            while (ex.getCause() != null)
                ex = ex.getCause();

            log.info("Sending HTTP.400 in answer to '/push-export' POST.");
            return Response.status(400).entity(ex.getMessage()).build();
        }

        // add config items from the received XML document to the repository
        try {
            ConfigMerger.getInstance().mergeConfiguration(receivedNetxmsConfig);
        } catch (RepositoryInitializationException | DatabaseException e) {
            Throwable ex = e;
            while (ex.getCause() != null)
                ex = ex.getCause();

            log.info("Sending HTTP.500 in answer to '/push-export' POST.");
            return Response.status(500).entity(ex.getMessage()).build();
        }

        log.info("Sending HTTP.200 in answer to '/push-export' POST.");
        return Response.ok().build();
    }
}
