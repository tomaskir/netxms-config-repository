package sk.atris.netxms.confrepo.data.model.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.VersionedEntity;
import sk.atris.netxms.confrepo.data.model.revision.ItemRevision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "traps")
@NoArgsConstructor
@Getter
public final class Trap extends VersionedEntity<ItemRevision> {

    @Column
    private String description;

    // Constructor
    public Trap(String guid, String description) {
        super(guid);
        this.description = description;
    }

}
