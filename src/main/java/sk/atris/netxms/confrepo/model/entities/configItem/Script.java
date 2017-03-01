package sk.atris.netxms.confrepo.model.entities.configItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.model.entities.configItem.ConfigItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scripts")
@NoArgsConstructor
public final class Script extends ConfigItem {
    @Column
    @Getter
    private String name;

    // Constructor
    public Script(String guid, String name) {
        super(guid);
        this.name = name;
    }
}
