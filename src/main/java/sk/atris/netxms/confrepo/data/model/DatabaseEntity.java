package sk.atris.netxms.confrepo.data.model;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
abstract class DatabaseEntity {

    @Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private long timestamp = System.currentTimeMillis() / 1000;

}
