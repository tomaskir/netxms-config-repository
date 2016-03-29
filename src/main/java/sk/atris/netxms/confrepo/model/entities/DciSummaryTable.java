package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public final class DciSummaryTable extends ConfigItem {
    @Getter
    private final String title;

    // Constructor
    public DciSummaryTable(String guid, String title) {
        super(guid);
        this.title = title;
    }
}
