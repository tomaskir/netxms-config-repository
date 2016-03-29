package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public final class Trap extends ConfigItem {
    @Getter
    private final String description;

    // Constructor
    public Trap(String guid, String description) {
        super(guid);
        this.description = description;
    }
}
