package sk.atris.netxms.confrepo.data.model.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.VersionedEntity;
import sk.atris.netxms.confrepo.data.model.revision.ItemRevision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
public final class Event extends VersionedEntity<ItemRevision> {

    @Column
    private String name;

    // Constructor
    public Event(String guid, String name) {
        super(guid);
        this.name = name;
    }

}
