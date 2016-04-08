package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.RevisionNotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Slf4j
public abstract class ConfigItem extends DatabaseEntity {
    @OneToMany
    private final List<Revision> revisions = new ArrayList<>();

    @Column
    @Getter
    protected final String guid;

    @Column(name = "next_version")
    private int nextRevisionVersion;

    // Constructor
    public ConfigItem(String guid) {
        log.trace("Creating a new '{}' object.", this.getClass().getSimpleName());

        this.guid = guid;
        this.nextRevisionVersion = 1;
    }

    /**
     * Return the next free revision version for this config item.
     *
     * @return int
     */
    @Synchronized
    public final int getNextRevisionVersion() {
        return nextRevisionVersion++;
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
    public final Revision getRevision(int requestedRevisionVersion) throws RevisionNotFoundException {
        log.trace("Getting revision '{}' of a '{}' object.", requestedRevisionVersion, this.getClass().getSimpleName());

        for (Revision r : revisions)
            if (r.getVersion() == requestedRevisionVersion)
                return r;

        log.warn("Requested revision '{}' of config item '{}' was not found!", requestedRevisionVersion, guid);
        throw new RevisionNotFoundException("Requested revision '" + requestedRevisionVersion + "' of config item '" + guid + "' not found!");
    }

    @Synchronized
    public final Revision getLatestRevision() {
        return revisions.get(revisions.size() - 1);
    }

    /**
     * Compare the provided GUID to the GUID of this ConfigItem
     *
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
