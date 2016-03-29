package sk.atris.netxms.confrepo.model.netxmsConfig;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.exceptions.ConfigItemNotFoundException;
import sk.atris.netxms.confrepo.model.entities.ConfigItem;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetxmsConfigRepository extends NetxmsConfig {
    @Getter
    private final static NetxmsConfigRepository instance = new NetxmsConfigRepository();

    @SuppressWarnings("unchecked")
    public final <T extends ConfigItem> T getConfigItemByGuid(String guid) throws ConfigItemNotFoundException {
        log.debug("Finding a config item with GUID '{}' in the NetxmsConfigRepository.", guid);

        try {
            return (T) dciSummaryTableRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) eppRuleRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) eventRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) objectToolRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) scriptRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) templateRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }
        try {
            return (T) trapRepository.getConfigItemByGuid(guid);
        } catch (ConfigItemNotFoundException ignored) {
        }

        log.warn("Config item with GUID '{}' not found in the NetxmsConfigRepository.", guid);
        throw new ConfigItemNotFoundException("Requested configuration item '" + guid + "' was not found in any repository!");
    }
}
