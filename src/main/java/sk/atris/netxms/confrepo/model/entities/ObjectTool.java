package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "object_tools")
public final class ObjectTool extends ConfigItem {
    @Column
    @Getter
    private final String name;

    // Constructor
    public ObjectTool(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
