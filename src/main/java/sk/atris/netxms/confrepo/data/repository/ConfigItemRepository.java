package sk.atris.netxms.confrepo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.atris.netxms.confrepo.data.model.ConfigItem;

@Repository
public interface ConfigItemRepository extends JpaRepository<ConfigItem, Long> {
    ConfigItem getByGuid(String guid);
}
