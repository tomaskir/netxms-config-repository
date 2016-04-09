package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "object_tools")
@NoArgsConstructor
public final class ObjectTool extends ConfigItem {
    @Column
    @Getter
    private String name;

    // Constructor
    public ObjectTool(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
