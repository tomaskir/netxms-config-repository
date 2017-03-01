package sk.atris.netxms.confrepo.data.model.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.VersionedEntity;
import sk.atris.netxms.confrepo.data.model.revision.ItemRevision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "epp_rules")
@NoArgsConstructor
@Getter
public final class EppRule extends VersionedEntity<ItemRevision> {

    @Column
    private String comment;

    // Constructor
    public EppRule(String guid, String comment) {
        super(guid);
        this.comment = comment;
    }

}
