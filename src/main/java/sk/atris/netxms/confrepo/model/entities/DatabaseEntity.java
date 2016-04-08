package sk.atris.netxms.confrepo.model.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class DatabaseEntity {
    @Column
    @Id
    @GeneratedValue
    private long id;
}
