package sk.atris.netxms.confrepo.model.entities;

import lombok.Getter;

public class Revision extends Entity {
    @Getter
    private final String xmlCode;

    @Getter
    private final String message;

    @Getter
    private final int version;

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
