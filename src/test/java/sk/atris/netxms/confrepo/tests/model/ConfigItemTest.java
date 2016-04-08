package sk.atris.netxms.confrepo.tests.model;

import org.junit.Test;
import sk.atris.netxms.confrepo.model.entities.ConfigItem;

public class ConfigItemTest {
    @Test
    public void testNextFreeVersion() {
        ConfigItem ci = new ConfigItem("guid") {
        };

        int rv1 = ci.getNextFreeRevisionVersion();
        int rv2 = ci.getNextFreeRevisionVersion();

        assert rv1 != rv2;
    }
}
