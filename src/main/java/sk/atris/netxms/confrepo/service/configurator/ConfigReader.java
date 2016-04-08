package sk.atris.netxms.confrepo.service.configurator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigReader {
    @Getter
    private final static ConfigReader instance = new ConfigReader();

    public String getReadOnlyAccessToken() {
        return getReadOnlyAccessToken(ApplicationConfiguration.CONFIG_FILE_NAME.toString());
    }

    /**
     * This method should be directly called only from tests!
     * For all other usage, call getReadOnlyAccessToken().
     */
    public String getReadOnlyAccessToken(String filename) {
        log.debug("Getting ReadOnly access token from '{}' property file on the classpath.", filename);

        return getPropertyValue(filename, ApplicationConfiguration.READONLY_ACCESS_TOKEN_PROPERTY.toString());
    }

    public String getReadWriteAccessToken() {
        return getReadWriteAccessToken(ApplicationConfiguration.CONFIG_FILE_NAME.toString());
    }

    /**
     * This method should be directly called only from tests!
     * For all other usage, call getReadWriteAccessToken().
     */
    public String getReadWriteAccessToken(String filename) {
        log.debug("Getting ReadWrite access token from '{}' property file on the classpath.", filename);

        return getPropertyValue(filename, ApplicationConfiguration.READWRITE_ACCESS_TOKEN_PROPERTY.toString());
    }

    private String getPropertyValue(String filename, String propertyName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            log.error("Error opening property file '{}'!", filename);
            return null;
        }

        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            log.error("Error '{}' loading the property file!", e.getMessage());
            return null;
        }

        String propertyValue = properties.getProperty(propertyName);

        if (propertyValue == null) {
            log.error("Property '{}' not found in the property file!", propertyName);
        } else if (propertyValue.equals("")) {
            // we do not allow empty property values, so if an empty property was read, treat it as if no property was present
            log.warn("Found empty property '{}' in the property file, ignoring it!", propertyName);
            propertyValue = null;
        }

        try {
            is.close();
        } catch (IOException ignored) {
        }

        return propertyValue;
    }
}
