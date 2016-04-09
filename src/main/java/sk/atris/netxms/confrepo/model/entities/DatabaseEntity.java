package sk.atris.netxms.confrepo.model.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor()
public abstract class DatabaseEntity {
    @Column
    @Id
    @GeneratedValue
    private long id;
}
