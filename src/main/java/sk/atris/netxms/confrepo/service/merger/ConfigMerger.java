package sk.atris.netxms.confrepo.service.merger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.exceptions.RepositoryInitializationException;
import sk.atris.netxms.confrepo.model.entities.configItem.*;
import sk.atris.netxms.confrepo.model.netxmsConfig.NetxmsConfigRepository;
import sk.atris.netxms.confrepo.model.netxmsConfig.ReceivedNetxmsConfig;
import sk.atris.netxms.confrepo.model.repository.Repository;
import sk.atris.netxms.confrepo.service.database.DbObjectHandler;

import javax.management.InstanceNotFoundException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigMerger {
    @Getter
    private final static ConfigMerger instance = new ConfigMerger();

    /**
     * Merge the provided {@link ReceivedNetxmsConfig} object into the repository.<br>
     * <br>
     * This will parse all the objects present in the repositories of the {@link ReceivedNetxmsConfig} object and:<br>
     * - add the config items not currently in the {@link NetxmsConfigRepository} to the {@link NetxmsConfigRepository}<br>
     * - for all config items already present in the {@link NetxmsConfigRepository}, add any newer revisions of any config item
     * (if they are present in the provided {@link ReceivedNetxmsConfig} object for that config item)<br>
     *
     * @param receivedNetxmsConfig object containing config items to be merged into the repository
     */
    @Synchronized
    public void mergeConfiguration(ReceivedNetxmsConfig receivedNetxmsConfig) throws DatabaseException, RepositoryInitializationException {
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

    private <T extends ConfigItem> void updateItemInRepository(T item) throws DatabaseException, RepositoryInitializationException {
        DbObjectHandler dbObjectHandler = DbObjectHandler.getInstance();

        try {
            ConfigItem repoItem = findItemInRepository(item);

            if (!latestRevisionXmlEquals(repoItem, item)) {
                log.info("Adding a new revision to a '{}' config item with GUID '{}' in the NetxmsConfigRepository.", item.getClass().getSimpleName(), item.getGuid());

                // We can not just add the ItemRevision object from the received item to the actual item, due to revision version uniqueness.
                // So we get the ItemRevision object of the received item...
                ItemRevision r = item.getLatestRevision();

                // Now we can build a new ItemRevision object here that has the values of the received ItemRevision object,
                // but with a correct version for the object in the config repository.
                ItemRevision newR = new ItemRevision(r.getXmlCode(), r.getMessage(), repoItem.getNextRevisionVersion());

                // now we can save the new revision to database, and add it to our item
                dbObjectHandler.saveToDb(newR);
                repoItem.addRevision(newR);

                // save the item changes to database
                try {
                    dbObjectHandler.saveToDb(repoItem);
                } catch (DatabaseException e) {
                    // if saving to database failed, remove the revision from the item
                    repoItem.removeRevision(newR);

                    // and throw the original exception
                    throw e;
                }
            }
        } catch (InstanceNotFoundException ignored) {
            log.info("Adding a new item with GUID '{}' to the '{}' repository of the NetxmsConfigRepository.", item.getGuid(), item.getClass().getSimpleName());

            // save all the revisions of the received item to database
            for (ItemRevision r : item.getRevisionsShallowCopy())
                dbObjectHandler.saveToDb(r);

            // now add the item to the config repository
            NetxmsConfigRepository.getInstance().addItem(item);
        }
    }

    private <T extends ConfigItem> T findItemInRepository(T item) throws InstanceNotFoundException, RepositoryInitializationException {
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
        ItemRevision latestRevisionItem1 = item1.getLatestRevision();
        ItemRevision latestRevisionItem2 = item2.getLatestRevision();

        log.trace("Comparing latest revisions of two '{}' class objects.", item1.getClass().getSimpleName());

        return latestRevisionItem1.xmlEquals(latestRevisionItem2);
    }
}
