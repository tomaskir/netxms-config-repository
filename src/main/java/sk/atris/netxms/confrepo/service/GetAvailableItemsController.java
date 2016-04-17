package sk.atris.netxms.confrepo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.atris.netxms.confrepo.exceptions.AccessTokenInvalidException;
import sk.atris.netxms.confrepo.exceptions.AccessTokenNotSetException;
import sk.atris.netxms.confrepo.service.auth.ReadAccessValidator;

@Slf4j
@RestController
@RequestMapping("/rest-api/get-available-items")
public final class GetAvailableItemsController {
    @Autowired
    ReadAccessValidator readAccessValidator;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getHandler(@RequestParam("accessToken") String providedAccessToken, @RequestBody String requestBody) {
        log.info("GET to '/get-available-items' received, processing it.");

        // TODO: rewrite using Spring Security and a role-based approach
        // validate access to the application
        try {
            readAccessValidator.check(providedAccessToken);
        } catch (AccessTokenInvalidException e) {
            log.warn("A request to '/get-available-items' was made with an invalid or missing access token!");

            log.info("Sending HTTP.403 in answer to '/get-available-items' GET.");
            return ResponseEntity.status(403).body("Missing or invalid access token.");
        } catch (AccessTokenNotSetException e) {
            String responseString = "No access tokens configured in 'application.properties', or the file was not found. " +
                    "Please configure a ReadOnly or ReadWrite token and restart the application.";

            log.info("Sending HTTP.500 in answer to '/get-available-items' GET.");
            return ResponseEntity.status(500).body(responseString);
        }

        return ResponseEntity.status(200).body(requestBody);
    }
}
