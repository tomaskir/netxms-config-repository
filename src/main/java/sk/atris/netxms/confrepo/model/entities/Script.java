package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scripts")
public final class Script extends ConfigItem {
    @Column
    @Getter
    private final String name;

    // Constructor
    public Script(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
