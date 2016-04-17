package sk.atris.netxms.confrepo.data.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "config_items")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public final class ConfigItem extends DatabaseEntity {
    @OneToMany
    private final List<Revision> revisions = new ArrayList<>();

    @Column
    protected String guid;

    @Column(name = "next_revision_version")
    private long nextRevisionVersion;

    @Column
    private String identifier;
}
