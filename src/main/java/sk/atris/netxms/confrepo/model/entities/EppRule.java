package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "epp_rules")
@NoArgsConstructor
public final class EppRule extends ConfigItem {
    @Column
    @Getter
    private String comment;

    // Constructor
    public EppRule(String guid, String comment) {
        super(guid);
        this.comment = comment;
    }
}
