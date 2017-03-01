package sk.atris.netxms.confrepo.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class Revision extends DatabaseEntity {

    @Column
    private int version;

    @Column
    private String message;

}
