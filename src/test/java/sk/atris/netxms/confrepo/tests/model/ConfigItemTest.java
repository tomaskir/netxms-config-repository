package sk.atris.netxms.confrepo.tests.model;

import org.junit.Test;
import sk.atris.netxms.confrepo.model.entities.configItem.ConfigItem;

public class ConfigItemTest {
    @Test
    public void testNextFreeVersion() {
        ConfigItem ci = new ConfigItem("guid") {
        };

        int rv1 = ci.getNextRevisionVersion();
        int rv2 = ci.getNextRevisionVersion();

        assert rv1 != rv2;
    }
}
