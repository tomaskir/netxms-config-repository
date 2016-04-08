package sk.atris.netxms.confrepo.tests.service.configuration;

import org.junit.Test;
import sk.atris.netxms.confrepo.service.configuration.ConfigReader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ConfigReaderTest {
    private final ConfigReader configReader = ConfigReader.getInstance();

    @Test
    public void testInvalidFile() throws ReflectiveOperationException {
        loadConfigFile("invalid.file");

        assert configReader.getDatabasePath() == null;
        assert configReader.getDatabaseUser() == null;
        assert configReader.getDatabasePass() == null;
        assert configReader.getReadOnlyAccessToken() == null;
        assert configReader.getReadWriteAccessToken() == null;
    }

    @Test
    public void testMissingProperties() throws ReflectiveOperationException {
        loadConfigFile("empty.properties");

        assert configReader.getDatabasePath() == null;
        assert configReader.getDatabaseUser() == null;
        assert configReader.getDatabasePass() == null;
        assert configReader.getReadOnlyAccessToken() == null;
        assert configReader.getReadWriteAccessToken() == null;
    }

    @Test
    public void testNoValueProperty() throws ReflectiveOperationException {
        loadConfigFile("no_value.properties");

        assert configReader.getDatabasePath() == null;
        assert configReader.getDatabaseUser() == null;
        assert configReader.getDatabasePass() == null;
        assert configReader.getReadWriteAccessToken() == null;
        assert configReader.getReadOnlyAccessToken() == null;
    }

    @Test
    public void testTokenLoading() throws ReflectiveOperationException {
        loadConfigFile("valid.properties");

        assert configReader.getDatabasePath().equals("db/hsql");
        assert configReader.getDatabaseUser().equals("user");
        assert configReader.getDatabasePass().equals("passwd");
        assert configReader.getReadWriteAccessToken().equals("test1");
        assert configReader.getReadOnlyAccessToken().equals("test2");
    }

    private void loadConfigFile(String filename) throws ReflectiveOperationException {
        Field f = configReader.getClass().getDeclaredField("configurationFile");

        f.setAccessible(true);

        // reset the final modifier
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);

        // set the new value
        f.set(configReader, filename);

        Method method = configReader.getClass().getDeclaredMethod("loadProperties");
        method.setAccessible(true);
        method.invoke(configReader);
    }
}
