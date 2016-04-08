package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "revisions")
public class Revision extends DatabaseEntity {
    @Column(name = "xml")
    @Getter
    private final String xmlCode;

    @Column
    @Getter
    private final String message;

    @Column
    @Getter
    private final int version;

    @Column
    @Getter
    private final long timestamp;

    // Constructor
    public Revision(String xmlCode, String message, int version) {
        this.xmlCode = xmlCode;
        this.message = message;
        this.version = version;

        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public boolean xmlEquals(Revision r) {
        return xmlCode.equals(r.getXmlCode());
    }
}
