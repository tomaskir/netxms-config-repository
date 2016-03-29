package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import java.time.Instant;

public class Revision extends Entity {
    @Getter
    private final String xmlCode;

    @Getter
    private final String revisionMessage;

    @Getter
    private final String timestamp;

    // Constructor
    public Revision(String xmlCode, String revisionMessage) {
        this.xmlCode = xmlCode;
        this.revisionMessage = revisionMessage;

        this.timestamp = String.valueOf(Instant.now().getEpochSecond());
    }

    public boolean xmlEquals(Revision r) {
        return xmlCode.equals(r.getXmlCode());
    }
}
