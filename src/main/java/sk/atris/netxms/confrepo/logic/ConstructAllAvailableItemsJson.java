package sk.atris.netxms.confrepo.logic;

import org.springframework.beans.factory.annotation.Autowired;
import sk.atris.netxms.confrepo.data.repository.ConfigItemRepository;

public final class ConstructAllAvailableItemsJson {
    @Autowired
    ConfigItemRepository configItemRepository;

    public void nieco() {
        configItemRepository.findAll();
    }
}
