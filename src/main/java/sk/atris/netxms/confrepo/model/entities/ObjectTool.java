package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public final class ObjectTool extends ConfigItem {
    @Getter
    private final String name;

    // Constructor
    public ObjectTool(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
