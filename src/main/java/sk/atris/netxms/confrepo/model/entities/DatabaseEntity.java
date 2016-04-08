package sk.atris.netxms.confrepo.model.entities;

import javax.persistence.*;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class DatabaseEntity {
    @Column
    @Id
    @GeneratedValue
    private long id;
}
