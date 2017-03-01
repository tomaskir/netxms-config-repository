package sk.atris.netxms.confrepo.model.entities.configItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.model.entities.DatabaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "item_revisions")
@NoArgsConstructor
public class ItemRevision extends DatabaseEntity {
    @Column(name = "xml", length = 65536)
    @Getter
    private String xmlCode;

    @Column
    @Getter
    private String message;

    @Column
    @Getter
    private int version;

    @Column
    @Getter
    private long timestamp;

    // Constructor
    public ItemRevision(String xmlCode, String message, int version) {
        this.xmlCode = xmlCode;
        this.message = message;
        this.version = version;

        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public boolean xmlEquals(ItemRevision r) {
        return xmlCode.equals(r.getXmlCode());
    }
}
