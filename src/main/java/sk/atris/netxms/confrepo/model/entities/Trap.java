package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "traps")
public final class Trap extends ConfigItem {
    @Column
    @Getter
    private final String description;

    // Constructor
    public Trap(String guid, String description) {
        super(guid);
        this.description = description;
    }
}
