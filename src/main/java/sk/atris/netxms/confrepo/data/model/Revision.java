package sk.atris.netxms.confrepo.data.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "revisions")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Revision extends DatabaseEntity {
    @Column(name = "config_item_id")
    private int configItemId;

    @Column
    private int version;

    @Column
    private long timestamp;

    @Column
    private String message;

    @Column(name = "xml", length = 65536)
    private String xmlCode;

    // Constructor
    public Revision(int configItemId, int version, String message, String xmlCode) {
        this.configItemId = configItemId;
        this.version = version;
        this.message = message;
        this.xmlCode = xmlCode;

        this.timestamp = System.currentTimeMillis() / 1000;
    }
}
