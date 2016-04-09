package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "templates")
@NoArgsConstructor
public final class Template extends ConfigItem {
    @Column
    @Getter
    private String name;

    // Constructor
    public Template(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
