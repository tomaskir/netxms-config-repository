package sk.atris.netxms.confrepo.model.entities.configItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.model.entities.configItem.ConfigItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "traps")
@NoArgsConstructor
public final class Trap extends ConfigItem {
    @Column
    @Getter
    private String description;

    // Constructor
    public Trap(String guid, String description) {
        super(guid);
        this.description = description;
    }
}
