package sk.atris.netxms.confrepo.model.repository;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.exceptions.DatabaseException;
import sk.atris.netxms.confrepo.model.entities.ConfigItem;
import sk.atris.netxms.confrepo.service.database.DbObjectHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class Repository<T extends ConfigItem> {
    private List<T> entities = new ArrayList<>();

    private final boolean persistent;

    // Constructor
    public Repository() {
        persistent = false;
    }

    // Constructor
    public Repository(boolean persistent) {
        this.persistent = persistent;
    }

    @Synchronized
    public void setEntities(List<T> entities) {
        log.debug("Setting a new config item list for a repository.");
        this.entities = entities;
    }

    @Synchronized
    public void addConfigItem(T object) throws DatabaseException {
        log.trace("Adding a new config item to a repository.");

        if (persistent)
            DbObjectHandler.getInstance().saveToDb(object);

        entities.add(object);
    }

    @Synchronized
    public int getConfigItemCount() {
        return entities.size();
    }

    @Synchronized
    public T getConfigItemByGuid(String guid) throws ConfigItemNotFoundException {
        log.trace("Config item with GUID '{}' was requested from a config repository, returning it.", guid);
        for (T entity : entities) {
            if (entity.getGuid().equals(guid)) {
                log.debug("Config item with GUID '{}' found in a config repository.", guid);
                return entity;
            }
        }

        log.trace("Config item with GUID '{}' was not found in a config repository.", guid);
        throw new ConfigItemNotFoundException();
    }

    @Synchronized
    public T getLastConfigItem() {
        log.trace("Last config item of a config repository was requested, returning it.");
        return entities.get(entities.size() - 1);
    }

    @Synchronized
    public List<T> getShallowCopy() {
        log.trace("A shallow copy of a config repository was requested, returning it.");

        return new ArrayList<>(entities);
    }

    /**
     * Clears the internal config items list.
     * Use with caution!
     */
    @Synchronized
    public void clearAllConfigItems() throws DatabaseException {
        log.warn("Clearing all config items from a config repository!");

        if (persistent)
            for (T entity : entities) {
                entity.clearAllRevisions();
                DbObjectHandler.getInstance().removeFromDb(entity);
            }

        entities.clear();
    }
}
