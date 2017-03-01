package sk.atris.netxms.confrepo.data.model.pack;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sk.atris.netxms.confrepo.data.model.VersionedEntity;
import sk.atris.netxms.confrepo.data.model.revision.PackageRevision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "config_packages")
@NoArgsConstructor
@Getter
public final class ConfigPackage extends VersionedEntity<PackageRevision> {

    @Column
    private String author;

    @Column
    private String description;

    // Constructor
    public ConfigPackage(String guid, String author, String description) {
        super(guid);
        this.author = author;
        this.description = description;
    }

}
