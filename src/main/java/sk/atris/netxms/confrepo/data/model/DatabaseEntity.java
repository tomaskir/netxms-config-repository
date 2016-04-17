package sk.atris.netxms.confrepo.data.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
abstract class DatabaseEntity {
    @Id
    @GeneratedValue
    private Long id;
}
