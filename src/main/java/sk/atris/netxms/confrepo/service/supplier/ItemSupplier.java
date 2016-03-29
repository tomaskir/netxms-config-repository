package sk.atris.netxms.confrepo.service.supplier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.model.entities.ConfigItem;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.model.util.RequestedConfigItem;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemSupplier {
    @Getter
    private static final ItemSupplier instance = new ItemSupplier();

    private final NetxmsConfigRepository netxmsConfigRepository = NetxmsConfigRepository.getInstance();

    public <T extends ConfigItem> String getItemsXml(List<RequestedConfigItem> requestedItems) throws ConfigItemNotFoundException {
        String finalXmlString = "";

        for (RequestedConfigItem requestedItem : requestedItems) {
           T foundRequestedItem = netxmsConfigRepository.getConfigItemByGuid(requestedItem.getGuid());

            // temp
            finalXmlString += foundRequestedItem.getLatestRevision().getXmlCode() + "\r\n";
        }

        // TODO: finish this

        // temp
        return finalXmlString;
    }
}
