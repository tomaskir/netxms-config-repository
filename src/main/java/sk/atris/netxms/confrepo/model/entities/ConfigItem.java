package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ConfigItem extends Entity {
    private final List<Revision> revisions = new ArrayList<>();

    @Getter
    protected final String guid;

    // Constructor
    public ConfigItem(String guid) {
        log.trace("Creating a new '{}' object.", this.getClass().getSimpleName());

        this.guid = guid;
    }

    @Synchronized
    public final void addRevision(Revision r) {
        log.trace("Adding a revision to a '{}' object.", this.getClass().getSimpleName());

        revisions.add(r);
    }

    @Synchronized
    public final int getRevisionCount() {
        return revisions.size();
    }

    @Synchronized
    public final List<Revision> getRevisionsShallowCopy() {
        log.trace("A shallow copy of a revision list for a '{}' config item was requested, returning it.", this.getClass().getSimpleName());

        return new ArrayList<>(revisions);
    }

    @Synchronized
    public final Revision getLatestRevision() {
        return revisions.get(revisions.size() - 1);
    }

    /**
     * Compare the provided GUID to the GUID of this ConfigItem
     * @param guid GUID to compare to this ConfigItem's GUID
     * @return boolean
     */
    public final boolean guidEquals(String guid) {
        return guid.equals(this.guid);

    }

    /**
     * Clears all the revisions from this object.
     * Use with caution!
     */
    @Synchronized
    public final void clearAllRevisions() {
        log.warn("Clearing all revisions of a '{}' config item!", this.getClass().getSimpleName());

        revisions.clear();
    }
}
