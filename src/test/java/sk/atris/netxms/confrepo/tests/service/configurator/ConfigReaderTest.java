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
        String token = configReader.getReadOnlyAccessToken("src/test/resources/empty.properties");

        assert token == null;
    }

    @Test
    public void testMissingProperties2() {
        String token = configReader.getReadWriteAccessToken("src/test/resources/empty.properties");

        assert token == null;
    }

    @Test
    public void testReadWriteTokenLoading() {
        String token = configReader.getReadWriteAccessToken("src/test/resources/accessToken.properties");

        assert token.equals("test1");
    }

    @Test
    public void testReadOnlyTokenLoading() {
        String token = configReader.getReadOnlyAccessToken("src/test/resources/accessToken.properties");

        assert token.equals("test2");
    }
}
