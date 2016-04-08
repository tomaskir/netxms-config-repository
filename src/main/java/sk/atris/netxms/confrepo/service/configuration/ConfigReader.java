package sk.atris.netxms.confrepo.service.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sk.atris.netxms.confrepo.enums.ApplicationConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public final class ConfigReader {
    @Getter
    private final static ConfigReader instance = new ConfigReader();

    private final String configurationFile = ApplicationConfiguration.CONFIG_FILE_NAME.toString();
    private Properties properties;

    // Constructor
    private ConfigReader() {
        loadProperties();
    }

    public String getReadOnlyAccessToken() {
        return getPropertyValue(ApplicationConfiguration.PROPERTY_RO_ACCESS_TOKEN.toString());
    }

    public String getReadWriteAccessToken() {
        return getPropertyValue(ApplicationConfiguration.PROPERTY_RW_ACCESS_TOKEN.toString());
    }

    public String getDatabasePath() {
        return getPropertyValue(ApplicationConfiguration.PROPERTY_DATABASE_PATH.toString());
    }

    public String getDatabaseUser() {
        return getPropertyValue(ApplicationConfiguration.PROPERTY_DATABASE_USER.toString());
    }

    public String getDatabasePass() {
        return getPropertyValue(ApplicationConfiguration.PROPERTY_DATABASE_PASS.toString());
    }

    private void loadProperties() {
        properties = new Properties();

        InputStream is = getClass().getClassLoader().getResourceAsStream(configurationFile);

        if (is == null) {
            log.error("Error opening property file '{}'!", configurationFile);
        } else {
            try {
                properties.load(is);
            } catch (IOException e) {
                log.error("Error '{}' loading the property file!", e.getMessage());
            }

            try {
                is.close();
            } catch (IOException e) {
                log.warn("Error '{}' closing the property file!", e.getMessage());
            }
        }
    }

    private String getPropertyValue(String propertyName) {
        String propertyValue = properties.getProperty(propertyName);

        if (propertyValue == null) {
            log.error("Property '{}' not found in the property file!", propertyName);
        } else if (propertyValue.equals("")) {
            // we do not allow empty property values, so if an empty property was read, treat it as if no property was present
            log.warn("Found empty property '{}' in the property file, ignoring it!", propertyName);
            propertyValue = null;
        }

        return propertyValue;
    }
}
