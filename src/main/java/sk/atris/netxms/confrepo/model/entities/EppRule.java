package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "epp_rules")
public final class EppRule extends ConfigItem {
    @Column
    @Getter
    private final String comment;

    // Constructor
    public EppRule(String guid, String comment) {
        super(guid);
        this.comment = comment;
    }
}
