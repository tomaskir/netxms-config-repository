package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public final class Event extends ConfigItem {
    @Column
    @Getter
    private final String name;

    // Constructor
    public Event(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
