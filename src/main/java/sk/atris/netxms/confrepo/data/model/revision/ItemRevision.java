package sk.atris.netxms.confrepo.data.model.revision;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.Revision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "item_revisions")
@NoArgsConstructor
@Getter
public class ItemRevision extends Revision {

    @Column(name = "xml", length = 65536)
    private String xmlCode;

    // Constructor
    public ItemRevision(int version, String message, String xmlCode) {
        super(version, message);
        this.xmlCode = xmlCode;
    }


}
