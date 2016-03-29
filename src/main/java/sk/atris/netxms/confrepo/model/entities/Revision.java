package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

import java.time.Instant;

public class Revision extends Entity {
    @Getter
    private final String xmlCode;

    @Getter
    private final String revisionMessage;

    @Getter
    private final int revisionVersion;

    @Getter
    private final long timestamp;

    // Constructor
    public Revision(String xmlCode, String revisionMessage, int revisionVersion) {
        this.xmlCode = xmlCode;
        this.revisionMessage = revisionMessage;
        this.revisionVersion = revisionVersion;

        this.timestamp = Instant.now().getEpochSecond();
    }

    public boolean xmlEquals(Revision r) {
        return xmlCode.equals(r.getXmlCode());
    }
}
