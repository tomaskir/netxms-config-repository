package sk.atris.netxms.confrepo.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.RevisionNotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Slf4j
@NoArgsConstructor
public abstract class VersionedEntity<T extends Revision> extends DatabaseEntity {

    @OneToMany(fetch = FetchType.EAGER)
    private final List<T> revisions = new ArrayList<>();

    @Column(name = "next_version")
    private int nextRevisionVersion = 1;

    @Column
    @Getter
    private String guid;

    // used by the Lombok @Synchronized annotation
    @Transient
    private final Object $lock = new Object();

    // Constructor
    public VersionedEntity(String guid) {
        this.guid = guid;
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
    public final void addRevision(T r) {
        log.trace("Adding a revision to a '{}' object.", this.getClass().getSimpleName());

        revisions.add(r);
    }

    @Synchronized
    public final void removeRevision(T r) {
        log.trace("Removing a revision from a '{}' object.", this.getClass().getSimpleName());

        revisions.remove(r);
    }

    @Synchronized
    public final int getRevisionCount() {
        return revisions.size();
    }

    @Synchronized
    public final List<T> getRevisionsShallowCopy() {
        log.trace("A shallow copy of a revision list for a '{}' config item was requested, returning it.", this.getClass().getSimpleName());

        return new ArrayList<>(revisions);
    }

    @Synchronized
    public final T getRevision(int requestedRevisionVersion) throws RevisionNotFoundException {
        log.trace("Getting revision '{}' of a '{}' object.", requestedRevisionVersion, this.getClass().getSimpleName());

        for (T r : revisions) {
            if (r.getVersion() == requestedRevisionVersion) {
                return r;
            }
        }

        log.warn("Requested revision '{}' of config item '{}' was not found!", requestedRevisionVersion, guid);
        throw new RevisionNotFoundException("Requested revision '" + requestedRevisionVersion + "' of config item '" + guid + "' not found!");
    }

    @Synchronized
    public final T getLatestRevision() {
        return revisions.get(revisions.size() - 1);
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VersionedEntity)) {
            return false;
        }

        final VersionedEntity other = (VersionedEntity) obj;

        return other.getGuid().equals(this.guid);
    }

}
