package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public final class Template extends ConfigItem {
    @Getter
    private final String name;

    // Constructor
    public Template(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
