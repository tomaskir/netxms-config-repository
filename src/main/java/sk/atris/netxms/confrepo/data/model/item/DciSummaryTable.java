package sk.atris.netxms.confrepo.data.model.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.VersionedEntity;
import sk.atris.netxms.confrepo.data.model.revision.ItemRevision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dci_summary_tables")
@NoArgsConstructor
@Getter
public final class DciSummaryTable extends VersionedEntity<ItemRevision> {

    @Column
    private String title;

    // Constructor
    public DciSummaryTable(String guid, String title) {
        super(guid);
        this.title = title;
    }

}
