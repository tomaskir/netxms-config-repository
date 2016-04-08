package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dci_summary_tables")
public final class DciSummaryTable extends ConfigItem {
    @Column
    @Getter
    private final String title;

    // Constructor
    public DciSummaryTable(String guid, String title) {
        super(guid);
        this.title = title;
    }
}
