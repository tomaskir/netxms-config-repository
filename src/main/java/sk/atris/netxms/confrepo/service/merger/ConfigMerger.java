package sk.atris.netxms.confrepo.service.merger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfig;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.model.repository.Repository;

import javax.management.InstanceNotFoundException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigMerger {
    @Getter
    private final static ConfigMerger instance = new ConfigMerger();

    @Synchronized
    public void mergeConfiguration(NetxmsConfig receivedNetxmsConfig) {
        log.debug("Starting a merge of the received configuration.");

        for (DciSummaryTable receivedItem : receivedNetxmsConfig.getRepository(DciSummaryTable.class).getShallowCopy()) {
            updateItemInRepository(receivedItem);
        }

        for (EppRule receivedItem : receivedNetxmsConfig.getRepository(EppRule.class).getShallowCopy()) {
            updateItemInRepository(receivedItem);
        }

        for (Event receivedItem : receivedNetxmsConfig.getRepository(Event.class).getShallowCopy()) {
            updateItemInRepository(receivedItem);
        }

        for (ObjectTool receivedItem : receivedNetxmsConfig.getRepository(ObjectTool.class).getShallowCopy()) {
            updateItemInRepository(receivedItem);
        }

        for (Script receivedItem : receivedNetxmsConfig.getRepository(Script.class).getShallowCopy()) {
            updateItemInRepository(receivedItem);
        }

        for (Template receivedItem : receivedNetxmsConfig.getRepository(Template.class).getShallowCopy()) {
            updateItemInRepository(receivedItem);
        }

        for (Trap receivedItem : receivedNetxmsConfig.getRepository(Trap.class).getShallowCopy()) {
            updateItemInRepository(receivedItem);
        }

        log.debug("Finished merging the received configuration.");
    }

    private <T extends ConfigItem> T findItemInRepository(T item) throws InstanceNotFoundException {
        log.trace("Finding an object of '{}' class in a repository.", item.getClass().getSimpleName());

        Repository<T> repository = NetxmsConfigRepository.getInstance().getRepository(item);

        // TODO: this could be handled directly by the repository
        for (T repositoryItem : repository.getShallowCopy()) {
            if (repositoryItem.guidEquals(item.getGuid()))
                return repositoryItem;
        }

        throw new InstanceNotFoundException();
    }

    private <T extends ConfigItem> boolean latestRevisionXmlEquals(T item1, T item2) {
        Revision latestRevisionItem1 = item1.getLatestRevision();
        Revision latestRevisionItem2 = item2.getLatestRevision();

        log.trace("Comparing latest revisions of two '{}' class objects.", item1.getClass().getSimpleName());

        return latestRevisionItem1.xmlEquals(latestRevisionItem2);
    }

    private <T extends ConfigItem> void updateItemInRepository(T item) {
        try {
            ConfigItem repoItem = findItemInRepository(item);

            if (!latestRevisionXmlEquals(repoItem, item)) {
                log.info("Adding a new revision to a '{}' config item with GUID '{}' in the NetxmsConfigRepository.", item.getClass().getSimpleName(), item.getGuid());

                // We can not just add the Revision object from the received item to the actual item, due to revision version uniqueness.
                // So we get the Revision object of the received item...
                Revision r = item.getLatestRevision();

                // Now we can build a new Revision object here that has the values of the received Revision object,
                // but with a correct version for the object in the config repository.
                repoItem.addRevision(new Revision(r.getXmlCode(), r.getRevisionMessage(), repoItem.getNextFreeRevisionVersion()));
            }
        } catch (InstanceNotFoundException ignored) {
            log.info("Adding a new item with GUID '{}' to the '{}' repository of the NetxmsConfigRepository.", item.getGuid(),item.getClass().getSimpleName());
            NetxmsConfigRepository.getInstance().addItem(item);
        }
    }
}
