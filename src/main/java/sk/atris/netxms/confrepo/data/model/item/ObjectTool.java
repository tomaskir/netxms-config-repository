package sk.atris.netxms.confrepo.data.model.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.VersionedEntity;
import sk.atris.netxms.confrepo.data.model.revision.ItemRevision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "object_tools")
@NoArgsConstructor
@Getter
public final class ObjectTool extends VersionedEntity<ItemRevision> {

    @Column
    private String name;

    // Constructor
    public ObjectTool(String guid, String name) {
        super(guid);
        this.name = name;
    }

}
