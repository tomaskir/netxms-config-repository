package sk.atris.netxms.confrepo.data.model.revision;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.Revision;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "package_revisions")
@NoArgsConstructor
@Getter
public class PackageRevision extends Revision {

    // GUID of config item, version of config item
    @ElementCollection
    private Map<String, Integer> itemMap;

    // Constructor
    public PackageRevision(int version, String message, Map<String, Integer> itemMap) {
        super(version, message);
        this.itemMap = itemMap;
    }

}
