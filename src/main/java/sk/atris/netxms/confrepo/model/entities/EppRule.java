package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public final class EppRule extends ConfigItem {
    @Getter
    private final String comment;

    // Constructor
    public EppRule(String guid, String comment) {
        super(guid);
        this.comment = comment;
    }
}
