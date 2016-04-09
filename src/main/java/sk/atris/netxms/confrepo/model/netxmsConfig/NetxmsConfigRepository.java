package sk.atris.netxms.confrepo.model.netxmsConfig;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.exceptions.RepositoryInitializationException;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.service.database.DbObjectHandler;

@Slf4j
public final class NetxmsConfigRepository extends NetxmsConfig {
    private final static NetxmsConfigRepository instance = new NetxmsConfigRepository();

    private boolean repositoryLoaded = false;

    // Constructor
    private NetxmsConfigRepository() {
        super(true);
    }

    public static NetxmsConfigRepository getInstance() throws RepositoryInitializationException {
        if (!instance.repositoryLoaded)
            instance.loadFromDb();

        return instance;
    }

    @SuppressWarnings("unchecked")
    @Synchronized
    private void loadFromDb() throws RepositoryInitializationException {
        if (!repositoryLoaded) {
            DbObjectHandler dbObjectHandler = DbObjectHandler.getInstance();

            log.info("Loading all configuration items from database.");
            try {
                dciSummaryTableRepository.setEntities(dbObjectHandler.getObjects(DciSummaryTable.class));
                eppRuleRepository.setEntities(dbObjectHandler.getObjects(EppRule.class));
                eventRepository.setEntities(dbObjectHandler.getObjects(Event.class));
                objectToolRepository.setEntities(dbObjectHandler.getObjects(ObjectTool.class));
                scriptRepository.setEntities(dbObjectHandler.getObjects(Script.class));
                templateRepository.setEntities(dbObjectHandler.getObjects(Template.class));
                trapRepository.setEntities(dbObjectHandler.getObjects(Trap.class));
            } catch (DatabaseException e) {
                log.error("Error loading configuration items from database - '{}'!", e.getMessage());
                throw new RepositoryInitializationException(e);
            }

            repositoryLoaded = true;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfigItem> T getConfigItemByGuid(String guid) throws ConfigItemNotFoundException {
        log.debug("Finding a config item with GUID '{}' in the NetxmsConfigRepository.", guid);

        try {
            return (T) dciSummaryTableRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) eppRuleRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) eventRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) objectToolRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) scriptRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) templateRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) trapRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }

        log.warn("Config item with GUID '{}' not found in the NetxmsConfigRepository.", guid);
        throw new ConfigItemNotFoundException("Requested configuration item '" + guid + "' was not found in any repository!");
    }
}
