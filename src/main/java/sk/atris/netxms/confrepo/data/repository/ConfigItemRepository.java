package sk.atris.netxms.confrepo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.atris.netxms.confrepo.data.model.VersionedEntity;

@Repository
public interface ConfigItemRepository extends JpaRepository<VersionedEntity, Long> {

    VersionedEntity getByGuid(String guid);

}
