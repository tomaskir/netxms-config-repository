package sk.atris.netxms.confrepo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Revision extends Entity {
    @Getter
    private final String xmlCode;

    @Getter
    private final String revisionMessage;

    public boolean xmlEquals(Revision r) {
        return xmlCode.equals(r.getXmlCode());
    }
}
