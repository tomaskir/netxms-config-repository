package sk.atris.netxms.confrepo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotSetException;
import sk.atris.netxms.confrepo.service.auth.ReadAccessValidator;
import sk.atris.netxms.confrepo.service.auth.WriteAccessValidator;

@Slf4j
@RestController
@RequestMapping("/rest-api/get-items")
public final class GetItemsController {
    @Autowired
    ReadAccessValidator readAccessValidator;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity postHandler(@RequestParam("accessToken") String providedAccessToken, @RequestBody String requestBody) {
        log.info("POST to '/get-items' received, processing it.");

        // TODO: rewrite using Spring Security and a role-based approach
        // validate access to the application
        try {
            readAccessValidator.check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/get-items' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/get-items' POST.");
            return ResponseEntity.status(403).body("Missing or invalid access token.");
        } catch (AccessTokenNotSetException e) {
            String responseString = "No access tokens configured in 'application.properties', or the file was not found. " +
                    "Please configure a ReadOnly or ReadWrite token and restart the application.";

            log.info("Sending HTTP.500 in answer to '/get-items' POST.");
            return ResponseEntity.status(500).body(responseString);
        }

        return ResponseEntity.status(200).body(requestBody);
    }
}
