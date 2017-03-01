package sk.atris.netxms.confrepo.model.entities.configItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.model.entities.configItem.ConfigItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dci_summary_tables")
@NoArgsConstructor
public final class DciSummaryTable extends ConfigItem {
    @Column
    @Getter
    private String title;

    // Constructor
    public DciSummaryTable(String guid, String title) {
        super(guid);
        this.title = title;
    }
}
