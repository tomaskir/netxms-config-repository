package sk.atris.netxms.confrepo.model.netxmsConfig;

import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.model.entities.*;
import sk.atris.netxms.confrepo.model.repository.Repository;

@Slf4j
public class NetxmsConfig {
    final Repository<DciSummaryTable> dciSummaryTableRepository = new Repository<>();
    final Repository<EppRule> eppRuleRepository = new Repository<>();
    final Repository<Event> eventRepository = new Repository<>();
    final Repository<ObjectTool> objectToolRepository = new Repository<>();
    final Repository<Script> scriptRepository = new Repository<>();
    final Repository<Template> templateRepository = new Repository<>();
    final Repository<Trap> trapRepository = new Repository<>();

    /**
     * This method is used to return a repository of objects of the the class of provided object.
     * This happens during run-time, since the class of the object is not known during compile-time.
     *
     * @param object The object of which's class repository should be returned
     * @param <T>    Subclass of {@link ConfigItem}
     * @return Repository&lt;T&gt;
     */
    @SuppressWarnings("unchecked")
    public final <T extends ConfigItem> Repository<T> getRepository(T object) {
        if (object instanceof DciSummaryTable)
            return (Repository<T>) dciSummaryTableRepository;
        if (object instanceof EppRule)
            return (Repository<T>) eppRuleRepository;
        if (object instanceof Event)
            return (Repository<T>) eventRepository;
        if (object instanceof ObjectTool)
            return (Repository<T>) objectToolRepository;
        if (object instanceof Script)
            return (Repository<T>) scriptRepository;
        if (object instanceof Template)
            return (Repository<T>) templateRepository;
        if (object instanceof Trap)
            return (Repository<T>) trapRepository;

        log.error("Attempted to get a repository of '{}' class, but such class repository is not supported!", object.getClass().getSimpleName());
        throw new RuntimeException("Could not find requested class repository!");
    }

    /**
     * This method is used to return a repository or objects of the provided class.
     * This happens during compile-time, since the compiler knows the provided class.
     *
     * @param clazz Class of which's repository should be returned
     * @param <T>   Subclass of {@link ConfigItem}
     * @return Repository&lt;T&gt;
     */
    @SuppressWarnings("unchecked")
    public final <T extends ConfigItem> Repository<T> getRepository(Class<T> clazz) {
        if (clazz == DciSummaryTable.class)
            return (Repository<T>) dciSummaryTableRepository;
        if (clazz == EppRule.class)
            return (Repository<T>) eppRuleRepository;
        if (clazz == Event.class)
            return (Repository<T>) eventRepository;
        if (clazz == ObjectTool.class)
            return (Repository<T>) objectToolRepository;
        if (clazz == Script.class)
            return (Repository<T>) scriptRepository;
        if (clazz == Template.class)
            return (Repository<T>) templateRepository;
        if (clazz == Trap.class)
            return (Repository<T>) trapRepository;

        log.error("Attempted to get a repository of '{}' class, but such class repository is not supported!", clazz.getSimpleName());
        throw new RuntimeException("Could not find requested class repository!");
    }

    public final <T extends ConfigItem> void addItem(T item) {
        getRepository(item).addConfigItem(item);
    }

    /**
     * Clears all the internal repositories.
     * Use with caution!
     */
    public final void clearAllConfig() {
        log.warn("Clearing all configuration from a '{}' object!", this.getClass().getSimpleName());

        getRepository(DciSummaryTable.class).clearAllConfigItems();
        getRepository(EppRule.class).clearAllConfigItems();
        getRepository(Event.class).clearAllConfigItems();
        getRepository(ObjectTool.class).clearAllConfigItems();
        getRepository(Script.class).clearAllConfigItems();
        getRepository(Template.class).clearAllConfigItems();
        getRepository(Trap.class).clearAllConfigItems();
    }
}
