package sk.atris.netxms.confrepo.model.repository;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.model.entities.ConfigItem;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class Repository<T extends ConfigItem> {
    private final List<T> entities = new ArrayList<>();

    @Synchronized
    public void addConfigItem(T object) {
        log.trace("Adding a new config item to a repository.");

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
            if (entity.getGuid().equals(guid))
                return entity;
        }

        log.debug("Config item with GUID '{}' was not found in a config repository.", guid);
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
    public void clearAllConfigItems() {
        log.warn("Clearing all config items from a config repository!");

        entities.clear();
    }
}
