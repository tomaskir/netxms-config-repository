package sk.atris.netxms.confrepo.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.ItemRequestJsonParserException;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestJsonParser {
    @Getter
    private static final ItemRequestJsonParser instance = new ItemRequestJsonParser();

    private final ObjectMapper mapper = new ObjectMapper();

    public List<RequestedConfigItem> parse(InputStream inputStream) throws IOException, ItemRequestJsonParserException {
        List<RequestedConfigItem> requestedConfigItems = new ArrayList<>();

        log.trace("Starting to parse a received InputStream.");
        JsonNode getItems = mapper.readTree(inputStream).get("get-items");

        if (getItems == null) {
            log.warn("Received 'get-items' request JSON didn't contain the 'get-items' object!");
            throw new ItemRequestJsonParserException("Request JSON didn't contain the 'get-items' object.");
        }

        log.debug("Starting to extract requested configuration from the received JSON.");

        if (getItems.isArray()) {
            for (JsonNode item : getItems) {
                String requestedGuid;
                int requestedRevisionVersion;

                // protects against missing objects
                try {
                    requestedGuid = item.get("guid").textValue();
                    requestedRevisionVersion = item.get("version").intValue();
                } catch (NullPointerException e) {
                    log.warn("One of the objects inside the received 'get-items' request JSON didn't contain the 'guid' or 'version' values!");
                    throw new ItemRequestJsonParserException("One of the objects inside the request JSON didn't contain the 'guid' or 'version' values.");
                }

                // protects against objects being invalid types
                if (requestedGuid == null || requestedRevisionVersion == 0) {
                    log.warn("One of the objects inside the received 'get-items' request JSON contained an invalid 'guid' or 'version' value!");
                    throw new ItemRequestJsonParserException("One of the objects inside the request JSON contained an invalid 'guid' or 'version' value.");
                }

                log.trace("Creating a new RequestedConfigItem object; guid '{}', revision version '{}'.", requestedGuid, requestedRevisionVersion);
                RequestedConfigItem configItem = new RequestedConfigItem(requestedGuid, requestedRevisionVersion);

                requestedConfigItems.add(configItem);
            }
        } else {
            log.warn("The 'get-items' object in the received 'get-items' request JSON didn't contain an object array!");
            throw new ItemRequestJsonParserException("The 'get-items' object in the request JSON didn't contain an object array.");
        }

        log.debug("Finished extracting requested configuration from the received JSON.");

        return requestedConfigItems;
    }
}
