package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public final class Script extends ConfigItem {
    @Getter
    private final String name;

    // Constructor
    public Script(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
