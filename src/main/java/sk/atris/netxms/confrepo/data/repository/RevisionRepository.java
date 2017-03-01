package sk.atris.netxms.confrepo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.atris.netxms.confrepo.data.model.Revision;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {

    Revision getByConfigItemIdAndVersion(int configItemId, int version);

}
