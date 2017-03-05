package sk.atris.netxms.confrepo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotSetException;
import sk.atris.netxms.confrepo.service.auth.WriteAccessValidator;

@Slf4j
@RestController
@RequestMapping("/rest-api/push-export")
public final class PushExportController {

    @Autowired
    private WriteAccessValidator writeAccessValidator;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.TEXT_XML_VALUE)
    public ResponseEntity postHandler(@RequestParam("accessToken") String providedAccessToken, @RequestBody String requestBody) {
        log.info("POST to '/push-export' received, processing it.");

        // TODO: rewrite using Spring Security and a role-based approach
        // validate access to the application
        try {
            writeAccessValidator.check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/push-export' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/push-export' POST.");
            return ResponseEntity.status(403).body("Missing or invalid access token.");
        } catch (AccessTokenNotSetException e) {
            String responseString = "No ReadWrite token configured in 'application.properties', or the file was not found. " +
                    "Please configure a ReadWrite token and restart the application.";

            log.info("Sending HTTP.500 in answer to '/push-export' POST.");
            return ResponseEntity.status(500).body(responseString);
        }

        return ResponseEntity.status(200).body(requestBody);
    }

}
