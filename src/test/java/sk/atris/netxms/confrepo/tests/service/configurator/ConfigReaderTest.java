package sk.atris.netxms.confrepo.tests.service.configurator;

import org.junit.Test;
import sk.atris.netxms.confrepo.service.configurator.ConfigReader;

public class ConfigReaderTest {
    private final ConfigReader configReader = ConfigReader.getInstance();

    @Test
    public void testInvalidFile1() {
        String token = configReader.getReadOnlyAccessToken("invalid.file");

        assert token == null;
    }

    @Test
    public void testInvalidFile2() {
        String token = configReader.getReadWriteAccessToken("invalid.file");

        assert token == null;
    }

    @Test
    public void testMissingProperties1() {
        String token = configReader.getReadOnlyAccessToken("empty.properties");

        assert token == null;
    }

    @Test
    public void testMissingProperties2() {
        String token = configReader.getReadWriteAccessToken("empty.properties");

        assert token == null;
    }

    @Test
    public void testNoValueProperty1() {
        String token = configReader.getReadWriteAccessToken("no_value.properties");

        assert token == null;
    }

    @Test
    public void testNoValueProperty2() {
        String token = configReader.getReadWriteAccessToken("no_value.properties");

        assert token == null;
    }

    @Test
    public void testReadWriteTokenLoading() {
        String token = configReader.getReadWriteAccessToken("accessToken.properties");

        assert token.equals("test1");
    }

    @Test
    public void testReadOnlyTokenLoading() {
        String token = configReader.getReadOnlyAccessToken("accessToken.properties");

        assert token.equals("test2");
    }
}
