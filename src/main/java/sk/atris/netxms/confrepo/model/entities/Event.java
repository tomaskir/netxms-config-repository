package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public final class Event extends ConfigItem {
    @Getter
    private final String name;

    // Constructor
    public Event(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
